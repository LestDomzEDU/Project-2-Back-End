# 🧰 Bets API PowerShell Test Script

This block contains one of each REST method (`GET`, `POST`, `PUT`, `DELETE`) for quick testing of your deployed Heroku API.

> 📦 Copy everything inside the PowerShell block below, paste it into your PowerShell terminal (Windows or macOS using `pwsh`), and hit **Enter**.

```powershell
# ============================
# Bets API Quick Test (PowerShell)
# ============================

# Base URL for your API
$baseUrl = "https://sportsbook-api-lester-efa829183023.herokuapp.com/api/bets"

# 🟢 GET - Retrieve all bets
Write-Host "`n--- GET: Retrieve all bets ---" -ForegroundColor Cyan
Invoke-RestMethod `
  -Uri $baseUrl `
  -Method GET | ConvertTo-Json -Depth 5

# 🟣 POST - Create a new bet
Write-Host "`n--- POST: Create a new bet ---" -ForegroundColor Cyan
$postBody = @{
  eventId = 1
  userId = 1
  selection = "HOME"
  oddsDecimal = 1.75
  stake = 10.00
  bettorRef = "ps-test"
}
$newBet = Invoke-RestMethod `
  -Uri $baseUrl `
  -Method POST `
  -Headers @{ "Content-Type" = "application/json" } `
  -Body ($postBody | ConvertTo-Json)
$newBet | ConvertTo-Json -Depth 5
$newId = $newBet.id
Write-Host "Created Bet ID: $newId"

# 🟠 PUT - Update the created bet (if ID returned)
if ($newId -ne $null) {
  Write-Host "`n--- PUT: Update bet $newId ---" -ForegroundColor Cyan
  $updateBody = @{
    selection = "AWAY"
    oddsDecimal = 2.0
    stake = 15.00
  }
  Invoke-RestMethod `
    -Uri "$baseUrl/$newId" `
    -Method PUT `
    -Headers @{ "Content-Type" = "application/json" } `
    -Body ($updateBody | ConvertTo-Json) | ConvertTo-Json -Depth 5
} else {
  Write-Host "⚠️  Skipping PUT (no ID returned from POST)" -ForegroundColor Yellow
}

# 🔴 DELETE - Remove the created bet (if ID returned)
if ($newId -ne $null) {
  Write-Host "`n--- DELETE: Remove bet $newId ---" -ForegroundColor Cyan
  Invoke-RestMethod `
    -Uri "$baseUrl/$newId" `
    -Method DELETE
  Write-Host "Deleted Bet $newId successfully.`n" -ForegroundColor Green
} else {
  Write-Host "⚠️  Skipping DELETE (no ID returned from POST)" -ForegroundColor Yellow
}
