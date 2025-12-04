###############################################
#                 PATH / DIR
###############################################
alias ..="cd .."
alias ...="cd ../.."
alias ....="cd ../../.."

alias home="cd ~"
alias docs="cd ~/Documents"
alias dl="cd ~/Downloads"
alias desk="cd ~/Desktop"

# smarter ls
if command -v eza >/dev/null 2>&1; then
  alias ls='eza --icons'
  alias ll='eza -l --icons'
  alias la='eza -a --icons'
  alias lla='eza -la --icons'
else
  alias l='ls'
  alias ll='ls -l'
  alias la='ls -a'
  alias lla='ls -la'
fi

on-zsh-git-search() {
    grep -n --color=auto "git remote -v" ~/.oh-my-zsh/plugins/git/git.plugin.zsh
}

###############################################
#                 Java / Maven
###############################################
# Java 环境切换
setjdk() {
    export JAVA_HOME="$1"
    export PATH="$JAVA_HOME/bin:$PATH"
    echo "JAVA_HOME → $JAVA_HOME"
}
alias jdk8='setjdk "$JAVA_8_HOME"'
alias jdk11='setjdk "$JAVA_11_HOME"'
alias jdk17='setjdk "$JAVA_17_HOME"'
alias jdk21='setjdk "$JAVA_21_HOME"'
alias jdk23='setjdk "$JAVA_23_HOME"'
alias jv='java -version'

# Maven
alias mvn='mvn'
alias mvcc='mvn clean compile'
alias mvc='mvn clean'
alias mvp='mvn package -DskipTests'
alias mvs='mvn spring-boot:run'


###############################################
#                 Docker
###############################################
alias dps='docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"'
alias dpa='docker ps -a --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"'
alias dimg='docker images'
alias dkill='docker kill'
alias drm='docker rm'
alias drmi='docker rmi'
alias dlog='docker logs -f'

alias dco='docker compose'
alias dcu='docker compose up -d'
alias dcd='docker compose down'

# 查看占用端口的容器
dport() { docker ps -a | grep "$1"; }


###############################################
#                 Redis 
###############################################
# 本地 redis-cli（如路径不同可改这里）
alias rcli='redis-cli'

# 查看某个 key
alias rget='redis-cli get'

# 搜索 key
rkeys() { redis-cli keys "*$1*"; }

# 监控实时命令
alias rmon='redis-cli monitor'


###############################################
#                 VS Code
###############################################
alias c='code .'     # 在当前目录打开 VSCode
alias vsc='code'
alias v='code .'

###############################################
#                 系统 / 运维工具
###############################################
alias cls='clear'
alias hist='history | grep'
alias top='htop'
alias dfh='df -h'
alias duh='du -h --max-depth=1'

alias psg='ps aux | grep'
alias killp='kill -9'
alias ports='lsof -i -P -n | grep LISTEN'

# 查看某端口
port() { 
    lsof -i tcp:"$1"; 
}

# 高亮搜索（grep 替代）
alias grep='grep --color=auto'


###############################################
#                 压缩 / 解压
###############################################
alias tgz='tar -czvf'
alias untgz='tar -xvzf'
alias untar='tar -xvf'
alias zipf='zip -r'


###############################################
#                 快速编辑 / 热加载
###############################################
alias zshrc='vim ~/.zshrc'
alias srcz='source ~/.zshrc'

###############################################
#                 Python
###############################################
alias py='python3'
alias pipi='pip3 install'
alias pyv='python3 --version'
alias pipu='pip3 install --upgrade pip'

# 快速创建 venv 并激活
pvenv() {
  python3 -m venv .venv
  source .venv/bin/activate
  echo "Activated venv → .venv"
}

# 进入当前目录的 venv
alias vact='source .venv/bin/activate'

# 查看 pip 安装包列表
alias pipls='pip list'


###############################################
#                 Node / npm / yarn / pnpm
###############################################
alias ni='npm install'
alias nr='npm run'
alias nrd='npm run dev'
alias nrb='npm run build'
alias nrs='npm start'

alias ya='yarn add'
alias yr='yarn run'
alias yb='yarn build'
alias ys='yarn start'

alias p='pnpm'
alias ppi='pnpm install'
alias pr='pnpm run'
alias pdev='pnpm dev'

# Node 版本查看
alias nodev='node -v'
alias npmv='npm -v'


# ###############################################
# #                 Rust
# ###############################################
# alias rc='cargo'
# alias rcb='cargo build'
# alias rcr='cargo run'
# alias rct='cargo test'
# alias rcc='cargo check'
# alias rcu='rustup update'
# alias rcv='rustc --version'

# # 创建项目
# alias rnew='cargo new'
# alias rinit='cargo init'


# ###############################################
# #                 Go (Golang)
# ###############################################
# alias gof='go fmt ./...'
# alias gob='go build'
# alias gor='go run'
# alias got='go test ./...'
# alias gov='go version'

# # Go module shortcuts
# alias gom='go mod'
# alias gomt='go mod tidy'
# alias gomi='go mod init'

# # 创建 go 项目
# gnew() {
#   mkdir -p "$1"
#   cd "$1" || return
#   go mod init "$1"
#   echo "package main\n\nfunc main() {}" > main.go
#   echo "Created Go project: $1"
# }
