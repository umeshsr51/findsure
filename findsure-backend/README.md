# FindSure backend — structure fix notes

## What was wrong

Every uploaded file's *name* didn't match its *content* — e.g. the file called
`UserRepository.java` actually contained the `ErrorResponse` DTO, and the file
called `AuthController.java` actually contained the `UserRepository`
interface. All 21 files were shuffled this way. None of it could compile,
because Java requires a public class's filename to match the class name.

## What this project does

1. **Renamed/relocated every file** to match its real class name and put it
   in the right package folder (`dto/`, `repository/`, `exception/`,
   `service/`, `util/`) — no logic was changed in these files.
2. **Reconstructed the pieces that were referenced but never actually
   uploaded**, needed for the existing code to compile at all:
   - `entity/User.java`, `entity/Item.java` — the JPA entities every DTO
     and repository method depends on.
   - `security/JwtUtil.java`, `JwtAuthFilter.java`, `SecurityConfig.java`,
     `CurrentUser.java` (+ its argument resolver), `WebConfig.java` —
     `AuthService` calls `jwtUtil.generateToken(...)` but no `JwtUtil` existed
     anywhere in what you sent.
   - `controller/AuthController.java` — wires `AuthService` to
     `POST /api/auth/register` and `POST /api/auth/login`.
   - `FindSureApplication.java` — the Spring Boot entry point.
   - `pom.xml`, `application.properties`, `env.example`, `.gitignore` —
     build/config files; these were also filenames holding unrelated
     exception classes in what you uploaded, so real ones didn't exist yet.
   - `schema-notes.sql` — matches the entities above; the version you sent
     also turned out to contain a Java exception class, not SQL.

## What's still open (by design, not a bug)

Per your own roadmap (`Phase 2 — Items`, `Phase 5 — Scan system`), these
aren't done yet and weren't faked here:

- **`ItemService` and `ItemController`** — the `ItemRepository` and all the
  Item DTOs exist and are ready, but no service/controller wires them to
  `/api/items` endpoints yet.
- **Lost/Found endpoints** (`POST /api/items/{id}/lost`, `.../found`).
- **Scan system** (Phase 5) — `Scan` entity, `ScanRepository`,
  `ScanController`, geolocation capture. `ItemResponse.lastScan` and
  `scanCount` are already shaped for this and currently return placeholders
  (`0` / `null`), matching what the original code comments said.
- **Finder-facing public endpoint** (`GET /api/public/items/{qrToken}`) —
  the safe, PII-free response the finder scan page needs.

## Before running it

- Create a MySQL database named `findsure` (or change the URL).
- Copy `env.example` → set real values, especially `JWT_SECRET`.
- `mvn spring-boot:run`

## Next step

Since the structure now compiles, the natural next move is finishing
**Phase 2 (Items)** — `ItemService` + `ItemController` — since everything
they depend on (`ItemRepository`, all Item DTOs, `QrTokenGenerator`) is
already in place.
