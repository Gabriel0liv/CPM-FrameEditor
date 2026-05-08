## Overview

CPM Timeline is a client-side addon for CustomPlayerModels that adds a timeline-style editor for animation frames. It is designed to make CPM animation work more like a visual timeline workflow, with frame scrubbing, frame reordering, frame coloring, and extra editor quality-of-life tools.

## The Problem

Editing animations inside CPM can be awkward when you need to:

1. Check frame timing quickly
2. Scrub through an animation while keeping the editor open
3. Reorder frames precisely
4. Organize larger animations with many frames and elements
5. Work with the CPM editor without losing context in the UI

CPM Timeline adds a dedicated timeline panel so those tasks are easier to manage.

## How It Works

The addon injects a timeline panel into the CPM animation editor and reads the current animation data directly from CPM. It then renders a visual frame timeline where you can inspect the animation, scrub playback, and move frames around.

It also adds a few editor-focused tools such as frame color tagging, selection helpers, pivot alignment actions, and quick grouping support for selected elements.

## Features

* Visual timeline inside the CPM animation editor
* Scrubbing and playback preview from the timeline
* Drag-and-drop frame reordering
* Per-frame color support
* Timeline visibility toggle
* Shift-based multi-selection in the editor
* Pivot alignment tools from the element context menu
* Quick group creation for selected elements

## Requirements

* Minecraft 1.20.1
* Forge 47.x
* CustomPlayerModels
* Java 17+

## Installation

1. Install CustomPlayerModels
2. Place the `cpm-timeline` jar in the `mods` folder
3. Start the game on the client
4. Open the CPM animation editor and use the new timeline panel

## Commands

This addon does not add commands. It works entirely inside the CPM editor UI.

## Compatibility

* This is a client-side addon
* It depends on CPM being present and loaded
* The addon relies on CPM editor internals and mixin hooks, so compatibility can vary if another addon modifies the same editor UI or animation flow

If another addon changes the same editor classes or animation handling, some features may work partially or may need updates.

## Why This Mod?

This addon is useful if you want CPM animation editing to feel less manual and more visual.

It helps keep:

* frame timing easier to read
* animation scrubbing more accessible
* frame ordering more direct
* editor actions more organized

## Project Structure

* `CPMTimelineAddon.java` - mod entry point
* `TimelinePanel.java` - timeline UI and frame interaction
* `TimelineAnimPanel.java` - integration layer for the CPM editor
* `mixin/` - editor injections and accessors
* `util/` - helper logic for layout, grouping, and element operations

## Credits

**Author**: tom / SatDPhoe

**License**: MIT
