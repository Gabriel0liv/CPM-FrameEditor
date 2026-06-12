## CPM Animator Utils 1.1.0

This release fixes compatibility with CustomPlayerModels `0.6.26+`.

CPM changed the `EditorAnim.animate` method signature in `0.6.26`, which caused CPM Animator Utils to crash when opening the CPM Editor. The mod now supports both the old and new CPM animation method signatures in the same build.

### Fixed
- Fixed crash when opening the CPM Editor with CPM `0.6.26+`.
- Fixed `InvalidInjectionException` in `EditorAnimMixin`.
- Restored compatibility with CPM versions before and after `0.6.26`.

### Improved
- Added dual descriptor-based mixin injection for old and new CPM versions.
- Added safer animation scrubbing logic to avoid secondary crashes.
- Improved frame index and duration handling during timeline scrubbing.

### Compatibility
- Supports CPM `0.6.24+`, including `0.6.25`, `0.6.26`, and `0.6.26a`.