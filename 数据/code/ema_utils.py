"""
ema_utils.py
-------------
封装多种计算 EMA（指数移动平均）的方法。

包含:
- 基于 NumPy 的递推实现
- 基于 Pandas 的实现

Python: 3.11+
依赖: numpy, pandas
"""
import matplotlib
import matplotlib.pyplot as plt
import numpy as np

matplotlib.rcParams['font.sans-serif'] = ['Hei']
matplotlib.rcParams['axes.unicode_minus'] = False


def line_ema_data(end_price=np.array([]), N=5):
    """
    计算线性 EMA（指数移动平均）。

    参数:
    N: 平滑窗口大小，默认 5

    返回:
    ema: 计算得到的 EMA 数组
    """
    print("原始收盘价:", end_price)
    weights = np.linspace(-1, 0, N)
    weights = weights / weights.sum()

    ema = np.convolve(weights, end_price)[N - 1:-N + 1]

    t = np.arange(N - 1, len(end_price))

    return t, ema


def line_ema(end_price=np.array([]), N=5):
    t, ema = line_ema_data(end_price, N)
    plt.plot(t, end_price[N - 1:], label='原始价格', lw=1.0)
    plt.plot(t, ema, label=f'{N}-日EMA', lw=2)
    plt.legend()
    plt.show()


def stand_ema_data(end_price=np.array([]), N=5):
    """
    计算标准 EMA（指数移动平均）。

    参数:
    N: 平滑窗口大小，默认 5

    返回:
    ema: 计算得到的 EMA 数组
    """
    print("原始收盘价:", end_price)
    # 2. 计算平滑系数 α
    alpha = 2 / (N + 1)
    # 3. 初始化 EMA 数组
    ema_values = np.zeros_like(end_price)
    ema_values[0] = end_price[0]  # 第一个值直接取原价
    # 4. 递推计算 EMA
    for i in range(1, len(end_price)):
        ema_values[i] = alpha * end_price[i] + (1 - alpha) * ema_values[i - 1]
    # 5. 输出 & 绘图
    t = np.arange(len(end_price))
    return t, ema_values


def stand_ema(end_price=np.array([]), N=5):
    t, ema_values = stand_ema_data(end_price, N)
    plt.plot(t, end_price, label='原始价格', lw=1.0)
    plt.plot(t, ema_values, label=f'{N}-日EMA', lw=2)
    plt.title(f'{N}-日指数移动平均 (EMA)')
    plt.legend()
    plt.show()
