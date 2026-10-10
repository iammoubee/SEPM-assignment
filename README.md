

### Observation

In TC1, the static counter and non-static total were both 1,000 in thread-safe and unsafe modes. The percentage difference was 0%.

In TC2, the thread-safe static counter correctly reached 20,000, matching the non-static total. However, the unsafe static counter produced different results across five runs, ranging from 13,269 to 17,128.

The percentage difference ranged from 14.36% to 33.66%, with an average of approximately 25.05%. This demonstrates that unsynchronized shared-variable updates can cause lost increments. Different thread scheduling and execution timing can produce different results across runs.

The experiment also demonstrates that a static variable is shared across objects, but it is not automatically thread-safe.
