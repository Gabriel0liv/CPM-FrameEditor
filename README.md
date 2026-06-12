# CPM Animator Utils

CPM Animator Utils is an advanced productivity suite designed for the CustomPlayerModels (CPM) animation environment. It enhances the native editor with a comprehensive set of visual tools, specialized timeline controls, and workflow optimizations intended to streamline the creation of complex character animations.

## Core Functionalities

### Integrated Timeline System
The mod introduces a dedicated, high-precision timeline panel directly into the CPM animation editor. This system allows animators to visualize the entire frame sequence, providing real-time scrubbing capabilities and intuitive frame management without disrupting the creative process.

### Workflow Enhancement Tools
*   **Dynamic Frame Management:** Supports rapid frame reordering and organizational tagging, allowing for better structure in large-scale animation projects.
*   **Visual Organization:** Implements per-frame color coding to assist in categorizing different animation states and transitions.
*   **Precise Alignment Utilities:** Features advanced pivot alignment actions and quick-grouping support for complex model elements, reducing manual adjustment time.
*   **Enhanced Selection Logic:** Introduces shift-based multi-selection within the editor to facilitate batch operations on animation elements.

## Usage Guide

### Activation
The utility suite is automatically integrated into the CustomPlayerModels animation editor interface upon installation. A new timeline panel will be available at the bottom of the editor screen.

### Controls
*   **Timeline Scrubbing:** Click and drag within the timeline sequence to preview animation states at specific intervals.
*   **Frame Reordering:** Select and drag individual frames to modify the animation sequence timing.
*   **Utility Context Menu:** Access specialized pivot and grouping tools by right-clicking elements within the CPM editor tree while the mod is active.

## Technical Specifications

*   **Platform:** Minecraft Forge & Fabric
*   **Version:** 1.20.1
*   **Required Dependencies:** CustomPlayerModels
*   **Environment:** Client-Side Only

## Compiling from Source

This repository uses independent Gradle projects per loader.

### Forge

```bash
cd CPMAnimatorUtils-1.20.1
./gradlew clean build
```

The output artifact will be created at: `CPMAnimatorUtils-1.20.1/build/libs/cpm_animator_utils-1.1.0.jar`

### Fabric

```bash
cd CPMAnimatorUtilsFabric-1.20.1
./gradlew clean build
```

The output artifact will be created at: `CPMAnimatorUtilsFabric-1.20.1/build/libs/cpm_animator_utils-fabric-1.1.0.jar`

### NeoForge

```bash
cd CPMAnimatorUtilsNeoForge-1.20.1
./gradlew clean build
```

The output artifact will be created at: `CPMAnimatorUtilsNeoForge-1.20.1/build/libs/cpm_animator_utils-neoforge-1.1.0.jar`

Building from the repository root is not supported.

### Compatibility & NeoForge 1.20.1 Note

Compatibility requirements differ per platform:

*   **Forge**: Requires CustomPlayerModels version `0.6.24` or newer.
*   **Fabric**: Requires CustomPlayerModels to be installed (version constraint set to `*` to bypass Fabric Loader SemVer validation errors with non-SemVer CPM releases like `0.6.26a`).
*   **NeoForge 1.20.1**: Requires CustomPlayerModels version `0.6.16c` or newer. 
    *   NeoForge 1.20.1 is a legacy environment identifying as `forge` internally, so the mod's platform dependency is configured as `"forge"`.
    *   To prevent class loading crashes on older CPM versions (like `0.6.16c`), the shared mixins do not reference `AnimationState` directly in signatures and instead use `@Coerce Object`.

## Installation

### Forge & Fabric
1. Ensure **CustomPlayerModels** is correctly installed in your Minecraft client (version `0.6.24` or newer, preferably `0.6.26a`).
2. Place the compiled jar matching your loader (Forge or Fabric) into the `mods` folder.
3. Launch Minecraft and access the timeline tools inside the CPM Animation Editor.

### NeoForge
1. Install **NeoForge 1.20.1** (version `47.1.106`).
2. Install **CustomPlayerModels NeoForge** (version `0.6.16c` or newer).
3. Place `cpm_animator_utils-neoforge-1.1.0.jar` into the `mods` folder.
4. Launch Minecraft and access the timeline tools inside the CPM Animation Editor.

## License

This software is released under the **MIT License**.

---
**Developed by SatDPhoe**
