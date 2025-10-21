# Running Tests for New-Bets

## Locally
```bash
./mvnw -q -DskipITs test
```

## On Heroku (CI)
If you're using Heroku CI or Review Apps, ensure `mvn -q -DskipITs test` runs in the build phase before packaging.
Heroku default build (via Procfile) runs the jar; tests run during `mvn package` when enabled.