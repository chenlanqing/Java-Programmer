#!/bin/bash

# UserPromptSubmit hook that forces explicit skill evaluation
#
# This hook requires Claude to explicitly evaluate each available skill
# before proceeding with implementation.
#
# Installation: Copy to .claude/hooks/UserPromptSubmit

cat <<'EOF'
INSTRUCTION: MANDATORY SKILL EVALUATION REQUIRED

Before proceeding with ANY implementation:

1. List EVERY skill from <available_skills>
2. For EACH skill, explicitly state:
   - Skill name
   - Is it relevant to this task? (YES/NO)
   - One sentence explaining why/why not

3. After evaluation, if ANY skills are relevant:
   - Use Skill() tool to activate ALL relevant skills
   - Wait for skill prompts to load
   - THEN proceed with implementation

4. If NO skills are relevant:
   - Explicitly state "No skills are relevant because [reason]"
   - THEN proceed with implementation

DO NOT SKIP THIS EVALUATION.
DO NOT START CODING UNTIL SKILLS ARE EVALUATED AND ACTIVATED.

This forces you to consciously check every available skill against the task requirements.
EOF