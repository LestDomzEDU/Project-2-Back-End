# =========================
# PowerShell API Test Script for Bets Endpoints
# =========================
# This script performs one of each REST call (GET, POST, PUT, DELETE)
# against the deployed Heroku API.
# Usage (Windows/macOS PowerShell):
#   pwsh ./bets_api_test.ps1

$baseUrl = "https://sportsbook-api-lester-efa829183023.herokuapp.com/api/bets"

Write-Host "🔹 GET: Retrieve all bets" -ForegroundColor Cyan
$response = Invoke-RestMethod -Uri $baseUrl -Method GET
$response | ConvertTo-Json -Depth 5
Write-Host "---------------------------------------------`n"

Write-Host "🔹 POST: Create a new bet" -ForegroundColor Cyan
$newBet = @{
    eventId = 1
    userId = 1
    selection = "HOME"
    oddsDecimal = 1.75
    stake = 10.00
    bettorRef = "script-test"
}
$postResponse = Invoke-RestMethod -Uri $baseUrl -Method POST -Headers @{ "Content-Type" = "application/json" } -Body ($newBet | ConvertTo-Json)
$postResponse | ConvertTo-Json -Depth 5
$newId = $postResponse.id
Write-Host "Created Bet ID: $newId"
Write-Host "---------------------------------------------`n"

if ($newId -ne $null) {
    Write-Host "🔹 PUT: Update the created bet" -ForegroundColor Cyan
    $updateBody = @{
        selection = "AWAY"
        oddsDecimal = 2.05
        stake = 20.00
    }
    $putUrl = "$baseUrl/$newId"
    $putResponse = Invoke-RestMethod -Uri $putUrl -Method PUT -Headers @{ "Content-Type" = "application/json" } -Body ($updateBody | ConvertTo-Json)
    $putResponse | ConvertTo-Json -Depth 5
    Write-Host "---------------------------------------------`n"

    Write-Host "🔹 DELETE: Remove the created bet" -ForegroundColor Cyan
    $deleteUrl = "$baseUrl/$newId"
    Invoke-RestMethod -Uri $deleteUrl -Method DELETE
    Write-Host "Bet $newId deleted successfully.`n"
} else {
    Write-Host "⚠️  No ID returned from POST; skipping PUT and DELETE tests." -ForegroundColor Yellow
}
