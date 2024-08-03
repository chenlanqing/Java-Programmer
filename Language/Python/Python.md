
Anaconda是⼀个开源的Python和R语⾔发⾏版，主要⾯向数据科学和机器学习任务

在使用pip3 安装 langchain 时，报错：WARNING: Retrying (Retry(total=4, connect=None, read=None, redirect=None, status=None)) after connection broken by 'SSLError(SSLCertVerificationError(1, '[SSL: CERTIFICATE_VERIFY_FAILED] certificate verify failed: unable to get local issuer certificate (_ssl.c:1007)'))': /simple/langchain/

使用 --trusted-host 选项绕过证书
```
pip3 install langchain --trusted-host pypi.org --trusted-host files.pythonhosted.org
```

# 参考资料

- [Python数学库](https://github.com/manimCommunity/manim)
- [Python Tutorial](https://www.pythontutorial.net/)
- [pythoncheatsheet](https://www.pythoncheatsheet.org/cheatsheet/string-formatting)
- [Python-dotenv](https://pypi.org/project/python-dotenv/)