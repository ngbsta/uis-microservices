# =====================================================================
#  prove-communication.ps1
#  Demonstrates that the 3 microservices really call each other.
#  Run AFTER the services are up (run-all.bat or docker compose up).
#
#  Communication cycle being proven:
#    lectures(8083) --> exam-registration(8081) --> e-study-record(8082) --> lectures(8083)
#
#  (Works on Windows PowerShell 5.1 — matches on raw JSON to avoid its array quirks.)
# =====================================================================

$exam     = "http://localhost:8081"
$estudy   = "http://localhost:8082"
$lectures = "http://localhost:8083"

function Section($t) { Write-Host "`n=== $t ===" -ForegroundColor Green }
function Pass($t)    { Write-Host "  [PROVEN] $t" -ForegroundColor Cyan }
function Fail($t)    { Write-Host "  [FAILED] $t" -ForegroundColor Red }
function GetText($url) { (Invoke-WebRequest -UseBasicParsing $url).Content }

# POST a registration; return @{ code = <http status>; body = <json> }
function PostReg($studentId, $sittingId) {
    $json = "{""studentId"":$studentId,""sittingId"":$sittingId}"
    try {
        $r = Invoke-WebRequest -UseBasicParsing "$exam/api/registrations" -Method Post `
                -ContentType "application/json" -Body $json
        return @{ code = [int]$r.StatusCode; body = $r.Content }
    } catch {
        $code = [int]$_.Exception.Response.StatusCode
        $body = ""
        try { $s = $_.Exception.Response.GetResponseStream(); $s.Position = 0
              $body = (New-Object IO.StreamReader($s)).ReadToEnd() } catch {}
        return @{ code = $code; body = $body }
    }
}

# ---------------------------------------------------------------------
Section "LINK 1  lectures(8083) -> exam-registration(8081)"
Write-Host "lectures owns NO exam sittings. GET /api/courses/2/exam-sittings must fetch"
Write-Host "them from exam-registration(8081)."
$body = GetText "$lectures/api/courses/2/exam-sittings"
Write-Host ("  lectures(8083) returned: " + $body)
if ($body -match '"courseId":2') {
    Pass "lectures served a sitting that only exam-registration owns -> it called 8081."
} else { Fail "no course-2 sitting returned by lectures." }

# ---------------------------------------------------------------------
Section "LINK 2  exam-registration(8081) -> e-study-record(8082)"
Write-Host "A registration is accepted only if e-study-record confirms enrolment."
Write-Host "Same sitting #1, two students -> opposite outcomes proves the call."

# 2a) enrolled student 1 (enrolled in course 1) -> passes the enrolment check
$a = PostReg 1 1
$enrolledOk = ($a.code -eq 200 -or $a.code -eq 201) -or ($a.body -match "already registered")
$aMsg = if ($a.body -match '"message":"([^"]+)"') { $matches[1] } else { "status $($a.code)" }
Write-Host ("  enrolled student 1  -> HTTP $($a.code)  ($aMsg)")

# 2b) NOT-enrolled student 999 -> rejected specifically by the enrolment prerequisite
$b = PostReg 999 1
$bMsg = if ($b.body -match '"message":"([^"]+)"') { $matches[1] } else { "status $($b.code)" }
Write-Host ("  not-enrolled 999    -> HTTP $($b.code)  ($bMsg)")
$notEnrolledRejected = ($b.body -match "not enrolled")

if ($enrolledOk -and $notEnrolledRejected) {
    Pass "outcome depended on e-study-record's enrolment data -> exam-reg called 8082."
} else { Fail "did not observe the enrolment-dependent asymmetry." }

# ---------------------------------------------------------------------
Section "LINK 3  e-study-record(8082) -> lectures(8083)"
Write-Host "e-study-record stores only a courseId. A class NAME on a grade can only come"
Write-Host "from lectures-service."
$results = GetText "$estudy/api/results?studentId=1"
$proxy   = GetText "$estudy/api/courses"
if ($results -match '"courseName":"([^"]+)"') {
    Write-Host ("  a graded row carries courseName='" + $matches[1] + "' (resolved from lectures)")
    Write-Host ("  /api/courses proxy (8082 -> 8083) returned: " + $proxy)
    Pass "e-study-record attached a real class name -> it called lectures(8083)."
} else { Fail "no course name resolved (is lectures-service up?)." }

Section "RESULT"
Write-Host "Cycle lectures -> exam-reg -> e-study-record -> lectures exercised end to end." -ForegroundColor Green
