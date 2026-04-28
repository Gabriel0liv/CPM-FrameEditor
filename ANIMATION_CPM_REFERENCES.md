# CPM (CustomPlayerModels) Animation System - Complete Reference Index

This document provides a comprehensive reference to all files within the CustomPlayerModels mod source that relate to animation systems, frames, attributes, timing, and associated functionality. Use this as a guide to locate specific implementation details when working with or modifying CPM's animation features.

## Source Root
All paths are relative to:  
`Referencia_CPM/CustomPlayerModels-master/CustomPlayerModels/src/`

---

## 1. Core Animation Classes (`shared/java/com/tom/cpm/shared/animation/`)

### Fundamental Animation Definitions
- `Animation.java`  
  Main animation class containing componentIDs, psfs (splines), show arrays, duration, frames, priority, and add flag.
  
- `AnimationState.java`  
  Runtime state of an animation including current time, playing status, and layering information.
  
- `IAnimation.java`  
  Interface defining the core animation contract.
  
- `AnimationNew.java`  
  Alternative or extended animation implementation (version-specific).
  
- `StagedAnimation.java`  
  Animation composed of multiple stages with different configurations.
  
- `StagedAnimInfo.java`  
  Information container for staged animations.
  
- `AnimationType.java`  
  Enum defining different animation types (likely for categorization).

### Animation Registry and Management
- `AnimationRegistry.java`  
  Central registry managing pose-to-triggers mapping, named actions, command actions, and custom poses.
  
- `AnimationHandler.java`  
  Handles animation playback, blending, and updates for entities.
  
- `AnimationTrigger.java`  
  Links a specific pose (IPose) to an Animation object for triggering.
  
- `IPose.java` / `VanillaPose.java` / `CustomPose.java`  
  Pose interface and implementations (vanilla Minecraft poses and user-defined custom poses).
  
- `ServerAnimationState.java`  
  Server-side representation of animation state for synchronization.

### Animation Parts and Components (`shared/java/com/tom/cpm/shared/parts/anim/`)

- `ModelPartAnimation.java` / `ModelPartAnimationNew.java`  
  Animation data specific to individual model parts.
  
- `AnimatorChannel.java`  
  Manages animation channels for different model parts or properties.
  
- `AnimationFrameData.java` / `AnimationFrameDataType.java`  
  Low-level frame data storage and type definitions.
  
- `ConstantTimeFloat.java` / `ConstantTimeBool.java`  
  Utility classes for time-based value interpolation.
  
- `Float3Driver.java`  
  Driver for animating 3D vectors (likely position/scale/rotation).
  
- `AnimLoaderState.java`  
  State during animation loading process.
  
- `LayerEncodingHelper.java`  
  Assists with encoding animation layers.
  
- `LegacyAnimationParser.java`  
  Parser for older animation formats.
  
- `SerializedAnimation.java` / `SerializedTrigger.java`  
  Data classes for animation serialization/deserialization.
  
- `ParameterDetails.java`  
  Details about animatable parameters.
  
- `TagType.java` / `StageType.java`  
  Enums for animation tagging and staging systems.

### Interpolation System (`shared/java/com/tom/cpm/shared/animation/interpolator/`)

- `Interpolator.java`  
  Base interface for all interpolators mapping input (0-1) to output values.
  
- `InterpolatorType.java`  
  Enum defining different interpolator algorithms.
  
- `LinearInterpolator.java`  
  Simple linear interpolation.
  
- `LinearLoopInterpolator.java`  
  Linear interpolation with looping behavior.
  
- `NoInterpolate.java`  
  Interpolator that returns constant values (no interpolation).
  
- `PolynomialSplineInterpolator.java`  
  Polynomial spline (e.g., Catmull-Rom) for smooth curves.
  
- `PolynomialSplineLoopInterpolator.java`  
  Polynomial spline with looping.
  
- `TrigonometricInterpolator.java`  
  Trigonometric-based interpolation.
  
- `TrigonometricLoopInterpolator.java`  
  Trigonometric interpolation with looping.
  
- `InterpolationInfo.java`  
  Metadata about interpolation configuration.
  
- `InterpolatorChannel.java`  
  Associates an interpolator with a specific animation channel/component.

### 2. Editor-Specific Animation (`shared/java/com/tom/cpm/shared/editor/anim/`)

- `EditorAnim.java`  
  **CRITICAL**: Editor's animation wrapper that stores frames as `AnimFrame` objects and converts to/from internal `Animation` splines. Contains:
  - `getFrames()`: Returns list of frames
  - `setSelectedFrame(AnimFrame)`: Synchronizes selection with editor
  - Conversion logic between user frames and internal spline representation
  
- `AnimFrame.java`  
  Represents a single editable frame in the editor containing raw transform data (position, rotation, scale) for all components.
  
- `AnimatedTex.java`  
  Handles animated textures within the editor.
  
- `AnimationDisplayData.java`  
  Data structure for how animation should be displayed in editor UI.
  
- `AnimationEncodingData.java`  
  Data for encoding animations (likely for saving/export).
  
- `AnimationProperties.java`  
  Properties panel data model for editing animation attributes.
  
- `IElem.java`  
  Interface for editable elements in animation editor.

### 3. Animation GUI Panels (`shared/java/com/tom/cpm/shared/editor/gui/`)

- `AnimPanel.java`  
  Main animation panel in CPM editor (where the timeline addon injects).
  
- `AnimTestPanel.java`  
  Panel for testing animations within the editor.
  
- `ViewportPanelAnim.java`  
  3D viewport panel specifically for animation preview.
  
- **Popup Windows**:
  - `AnimationSettingsPopup.java`  
    Main animation settings dialog.
  - `AnimEncConfigPopup.java`  
    Animation encoding configuration popup.

### 4. Animation Project Loaders (`shared/java/com/tom/cpm/shared/editor/project/loaders/`)

- `AnimationsLoaderV1.java`  
  Version 1 loader for animation project files.

### 5. Animation Networking (`shared/java/com/tom/cpm/shared/network/packet/`)

- `ServerAnimationS2C.java`  
  Server-to-client packet for synchronizing animation state.

### 6. Animation Menus and Gestures (`shared/java/com/tom/cpm/shared/parts/anim/menu/`)

These files define the UI elements for animation-triggering gestures and dropdowns:
  
- `AbstractDropdownButtonData.java` / `AbstractGestureButtonData.java`  
  Base classes for dropdown and gesture buttons.
  
- `DropdownButtonData.java`  
  Standard dropdown button data.
  
- `GestureButtonType.java`  
  Enum for different gesture button types.
  
- `LegacyDropdownButtonData.java`  
  Backward-compatible dropdown data.
  
- `ValueParameterButtonData.java` / `SimpleParameterValueAction.java`  
  UI for triggering animations based on parameter values.
  
- `BoolParameterToggleButtonData.java`  
  Toggle button for boolean parameter-triggered animations.
  
- `BitmaskParameterValueAction.java`  
  Action for bitmask parameter values.
  
- `CommandAction.java`  
  Triggers animation via chat command.
  
- `CustomPoseGestureButtonData.java`  
  Gesture button for custom poses.
  
- `SkinLayerParameterValueAction.java`  
  Triggers animation based on skin layer visibility.
  
- `ParameterDetails.java`  
  Details about parameters used in menu actions.

### 7. Texture Animation (`shared/java/com/tom/cpm/shared/animation/`)

- `AnimatedTexture.java`  
  Handles texture animations (separate from model animations).

### 8. Utility and Support Classes

- `HandAnimation.java` (`shared/java/com/tom/cpl/util/`)  
  Handles hand/item animations (likely for held items).
  
- `AnimationExporter.java` (`shared/java/com/tom/cpm/shared/editor/`)  
  Exports animations to external formats.

---

## 9. Key File Relationships and Data Flow

### Animation Creation and Editing Flow
1. **User Interaction**: 
   - User creates/edits frames in CPM editor via `AnimPanel`
   - Editor stores frames as `AnimFrame` objects in `EditorAnim`
   
2. **Internal Conversion**:
   - On save/project update: `EditorAnim` converts `AnimFrame[]` → `Animation` with spline data (`psfs[][]`)
   - On load: `Animation` spline data → rebuilt `AnimFrame[]` for editor
   
3. **Runtime Playback**:
   - `AnimationRegistry` triggers animations based on entity poses (`IPose`)
   - `AnimationHandler` updates `AnimationState` each tick
   - `Interpolator` objects evaluate splines at current time position
   - Final transforms applied to model parts via `AnimatorChannel`

### Critical Access Points for External Mods
- `EditorAnim.getFrames()` → List of `AnimFrame` (for reading frame data)
- `EditorAnim.setSelectedFrame(AnimFrame)` → Sync editor selection
- `AnimationRegistry.poseToTriggers` → Access pose-triggered animations
- `Animation.duration` and `Animation.frames` → Timing properties
- `Animation.psfs[][]` → Internal spline data (advanced access)

---

## 10. How to Use This Reference

### To Find:
- **Frame Data**: Look at `EditorAnim.java` and `AnimFrame.java`
- **Timing/Duration**: Check `Animation.java` (`duration`, `frames` fields)
- **Spline/Interpolation**: Examine `interpolator/` package and `Animation.psfs`
- **Pose Triggering**: Review `AnimationRegistry.java` and `IPose` implementations
- **Editor Integration**: Study `Animator.java` (editor facade) and `EditorAnim.java`
- **GUI Panels**: Check `editor/gui/` for panels like `AnimPanel.java`
- **Network Sync**: See `network/packet/ServerAnimationS2C.java`
- **Gesture System**: Explore `parts/anim/menu/` for trigger mechanisms

### Version Notes
This reference covers the source structure as of the referenced CPM version. Different CPM forks/branches (Forge, Fabric, etc.) may have slight variations, but core animation system remains consistent.

*Last updated: 2026-04-25*  
*For use with CustomPlayerModels source code navigation*