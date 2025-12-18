参考文章：  
[How to Make Claude Code Skills Activate Reliably](https://scottspence.com/posts/how-to-make-claude-code-skills-activate-reliably)  
译文：https://mp.weixin.qq.com/s/AdDjKJAD1146LIAL3awT4g

Create file: `~/.claude/hooks/skill-forced-eval-hook.sh`  
Make it executable: `chmod +x skill-forced-eval-hook.sh`  
Add to `.claude/settings.json`
```json
{
	"hooks": {
		"UserPromptSubmit": [
			{
				"hooks": [
					{
						"type": "command",
						"command": "~/.claude/hooks/skill-forced-eval-hook.sh"
					}
				]
			}
		]
	}
}
```

[skill-forced-eval-hook.sh](https://github.com/spences10/svelte-claude-skills/blob/main/test-bed-results/skill-forced-eval-hook.sh) 中文内容：
```sh
#!/bin/bash
# UserPromptSubmit hook - 强制技能评估

cat <<'EOF'
指令：强制技能激活流程

步骤 1 - 评估（必须在响应中完成）：
针对 <available_skills> 中的每个技能，陈述：[技能名] - 是/否 - [理由]

步骤 2 - 激活（紧接着步骤 1 立即执行）：
如果任何技能为"是" → 立即为每个相关技能使用 Skill(技能名) 工具
如果所有技能为"否" → 说明"不需要技能"并继续

步骤 3 - 实现：
只有在步骤 2 完成后，才能开始实现。

关键：你必须在步骤 2 调用 Skill() 工具。不要跳过直接实现。
评估（步骤 1）如果不激活（步骤 2）就毫无价值。

正确流程示例：
- research: 否 - 不是研究任务
- svelte5-runes: 是 - 需要响应式状态
- sveltekit-structure: 是 - 需要创建路由

[然后立即使用 Skill() 工具：]
> Skill(svelte5-runes)
> Skill(sveltekit-structure)

[只有完成这些之后才开始实现]
EOF
```