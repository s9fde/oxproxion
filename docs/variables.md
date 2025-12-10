# Variable Substitution

Oxproxion supports runtime variable substitution in prompts (both system and user messages) before they are sent to the LLM.

## Available Variables

### {{date}}
- **Format**: YYYY-MM-DD (ISO 8601 local date)
- **Example**: `2025-12-10`
- **Description**: Replaced with the current date at the time the message is sent

### {{time}}
- **Format**: HH:MM (24-hour local time, hours and minutes only)
- **Example**: `15:04`
- **Description**: Replaced with the current time at the time the message is sent

## Usage

Variables are case-insensitive and can include spaces around the variable name:
- `{{date}}` ✓
- `{{DATE}}` ✓
- `{{ date }}` ✓
- `{{ Time }}` ✓

## Examples

**User prompt:**
```
What's the weather like today, {{date}}?
```

**System message:**
```
You are a helpful assistant. Today's date is {{date}} and the current time is {{time}}.
```

## Implementation Notes

- Variable substitution happens at send-time only (right before the network call)
- Original messages in the UI and stored chat history remain unchanged
- Substituted values use the local system time when the message is sent
- No timezone offset is included in the time format
