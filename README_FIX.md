# Heroku build fix (Spring Security + OAuth classes not found)

The errors like `cannot find symbol: class HttpSecurity` and `class SecurityFilterChain` mean the Security
dependencies are missing from your classpath.

## Do this:
1) Open your project's `pom.xml` and paste the contents of `pom-dependency-snippet.xml` **inside** the existing
   `<dependencies> ... </dependencies>` block.
2) Put the `system.properties` file in your repo root to pin Java 17 on Heroku.
3) Commit and push, then redeploy.

Optional: if tests fail your build, add `-DskipTests` to your Heroku build command or temporarily disable tests.