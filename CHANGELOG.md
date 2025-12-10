# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added
- Simple `{{date}}` and `{{time}}` variable substitution for prompts
  - Date format: YYYY-MM-DD
  - Time format: HH:MM (24-hour local time)
  - Variables are replaced at send-time only, preserving original messages in UI and storage
  - Case-insensitive matching with optional whitespace support
  - Works in both system and user messages
  - Applied to title suggestion prompts as well
