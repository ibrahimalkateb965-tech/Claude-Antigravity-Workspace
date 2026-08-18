---
name: test-guard
description: Review generated or changed test code against universal testing rules before it ships. Best used reactively after an agent writes, edits, generates, or refactors tests, before presenting, committing, or merging them. Use for JUnit/MockK/Robolectric (*Test.kt), Jest/Vitest (*.test.js), or any test suite to prevent AI-generated test bloat, mock abuse, and empty assertions.
---

# Test Guard

You are reviewing generated or changed test code before it ships. Enforce the rules below to prevent AI test bloat and false-positive test suites.

## Core Rules for Test Verification

1. **Test Behavior, Not Implementation**:
   - Assert observable outputs and state changes from the caller's perspective.
   - Do NOT assert that internal private helpers were invoked with exact internal parameters unless part of an explicit contract.

2. **No Mock Abuse**:
   - Only mock external boundaries (Network APIs, System Clock, Hardware Sensors, Third-Party SDKs).
   - Never mock pure business logic, data models, or in-memory data structures.

3. **No Tautological or Empty Tests**:
   - Every test MUST fail if the underlying production code logic is broken or mutated.
   - Prohibit tests that assert `assertTrue(true)` or only test mocked behaviors returning mocked values.

4. **One Logical Concern Per Test**:
   - Each test method should verify a single scenario or state transition.
   - Name tests clearly: `givenCondition_whenAction_thenExpectedOutcome`.

5. **Deterministic Execution**:
   - Eliminate flaky time-based delays (`Thread.sleep()`). Use virtual time, test dispatchers, or explicit coroutine delays.
   - Tests must run independently in any order without shared mutable state.

## Delivery Checklist
- [ ] What specific bug does this test catch that no other test catches?
- [ ] Does the test test behavior instead of implementation details?
- [ ] Are mock boundaries strictly limited to external I/O?
