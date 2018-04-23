


1.将double或者float数据转换为 BigDecimal 类型,最好使用 BigDecimal.valueOf(), 而不应该使用 new BigDecimal.
    --> new BigDecimal(0.1)会把0.1当成: 0.1000000000000000055511151231257827021181583404541015625
    --> valueOf:其内部使用了String作为构造函数的入参，所以使用valueOf后0.1还是0.1
  