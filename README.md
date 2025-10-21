*Powershell Get Method:*<br>
Invoke-RestMethod `<br>
  -Uri "https://sportsbook-api-lester-efa829183023.herokuapp.com/api/bets" `<br>
  -Method GET<br>
<br><br>
  *Powershell Post Method:*<br>
  Invoke-RestMethod `<br>
  -Uri "https://sportsbook-api-lester-efa829183023.herokuapp.com/api/bets" `<br>
  -Method POST `<br>
  -Headers @{ "Content-Type" = "application/json" } `<br>
  -Body (@{
      eventId = 1
      userId = 1
      selection = "HOME"
      oddsDecimal = 1.75
      stake = 10.00
      bettorRef = "ps-test"
  } | ConvertTo-Json)<br>
<br><br>
  *Powershell Put Method:*<br>
  Invoke-RestMethod `<br>
  -Uri "https://sportsbook-api-lester-efa829183023.herokuapp.com/api/bets/3" `<br>
  -Method PUT `<br>
  -Headers @{ "Content-Type" = "application/json" } `<br>
  -Body (@{
      selection = "AWAY"
      oddsDecimal = 2.0
      stake = 15.00
  } | ConvertTo-Json)<br>
<br>
  *Powershell DELETE Method:*<br>
  Invoke-RestMethod `<br>
  -Uri "https://sportsbook-api-lester-efa829183023.herokuapp.com/api/bets/1" `<br>
  -Method DELETE
