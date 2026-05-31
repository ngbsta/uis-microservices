# ============================================================
#  UIS microservices - one-time setup: installs JDK 17 + Maven
#  Run via setup.bat (double-click). Needs internet.
# ============================================================

function Add-UserPath($dir) {
    $p = [Environment]::GetEnvironmentVariable("Path", "User")
    if ($p -notlike "*$dir*") {
        [Environment]::SetEnvironmentVariable("Path", "$p;$dir", "User")
        Write-Host "  PATH += $dir"
    }
}

Write-Host "=== 1/2  Installing JDK 17 (Eclipse Temurin) via winget ===" -ForegroundColor Cyan
try {
    winget install -e --id EclipseAdoptium.Temurin.17.JDK --accept-source-agreements --accept-package-agreements
} catch {
    Write-Host "winget failed. Install JDK 17 manually from https://adoptium.net" -ForegroundColor Yellow
}

$jdk = Get-ChildItem "C:\Program Files\Eclipse Adoptium" -Directory -Filter "jdk-17*" -ErrorAction SilentlyContinue | Select-Object -First 1
if ($jdk) {
    [Environment]::SetEnvironmentVariable("JAVA_HOME", $jdk.FullName, "User")
    Add-UserPath "$($jdk.FullName)\bin"
    Write-Host "  JAVA_HOME = $($jdk.FullName)" -ForegroundColor Green
} else {
    Write-Host "  Could not locate JDK 17 folder - set JAVA_HOME manually." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=== 2/2  Installing Maven 3.9.9 ===" -ForegroundColor Cyan
$mvnVer  = "3.9.9"
$mvnRoot = "$env:USERPROFILE\apache-maven"
$mvnHome = "$mvnRoot\apache-maven-$mvnVer"
if (-not (Test-Path "$mvnHome\bin\mvn.cmd")) {
    $url = "https://archive.apache.org/dist/maven/maven-3/$mvnVer/binaries/apache-maven-$mvnVer-bin.zip"
    $zip = "$env:TEMP\maven.zip"
    Write-Host "  Downloading $url"
    Invoke-WebRequest -Uri $url -OutFile $zip
    Expand-Archive -Path $zip -DestinationPath $mvnRoot -Force
}
if (Test-Path "$mvnHome\bin\mvn.cmd") {
    Add-UserPath "$mvnHome\bin"
    Write-Host "  Maven = $mvnHome" -ForegroundColor Green
} else {
    Write-Host "  Maven install failed - download manually from https://maven.apache.org" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=== DONE ===" -ForegroundColor Green
Write-Host "IMPORTANT: close this window, then run 'run-all.bat' (so the new PATH is loaded)."
