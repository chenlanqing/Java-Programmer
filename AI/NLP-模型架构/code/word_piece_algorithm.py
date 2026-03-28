from collections import defaultdict

def create_new_symbol(x, y):
    """
    根据 WordPiece 的规则创建新符号。

    - 如果 y 以 '##' 开头，合并时需要去掉 y 的 '##' 前缀。
    - 新符号是否以 '##' 开头，取决于 x 是否以 '##' 开头。
    """
    x_starts_hash = x.startswith('##')
    x_without_hash = x[2:] if x_starts_hash else x
    y_without_hash = y[2:] if y.startswith('##') else y
    new_symbol = x_without_hash + y_without_hash
    if x_starts_hash:
        new_symbol = '##' + new_symbol
    return new_symbol

def count_char_pairs_wordpiece(word_freq):
    """
    计算字符对的频次和单个字符的频次。
    
    参数：
        word_freq: List of tuples, 每个元组包含单词（列表形式）和其频次
        
    返回：
        两个字典，分别为字符对频次和单个字符频次
    """
    pair_freq = defaultdict(int)
    char_freq = defaultdict(int)
    for word, freq in word_freq:
        for i in range(len(word)):
            char_freq[word[i]] += freq
            if i < len(word) - 1:
                pair = (word[i], word[i + 1])
                pair_freq[pair] += freq
    return pair_freq, char_freq

def compute_wordpiece_score(freq_xy, freq_x, freq_y):
    """
    根据 WordPiece 的定义计算 Score。
    
    参数：
        freq_xy: 符号对的频次
        freq_x: 符号 x 的频次
        freq_y: 符号 y 的频次
        
    返回：
        计算得到的 Score
    """
    if freq_x == 0 or freq_y == 0:
        return 0
    return freq_xy / (freq_x * freq_y)

def find_best_pair_wordpiece(pair_freq, char_freq):
    """
    找到具有最高 Score 的字符对。

    参数：
        pair_freq: 字符对频次的字典
        char_freq: 单个字符频次的字典
        
    返回：
        具有最高 Score 的字符对及其 Score
    """
    scores = {}
    for pair, freq_xy in pair_freq.items():
        x, y = pair
        freq_x = char_freq.get(x, 0)
        freq_y = char_freq.get(y, 0)
        score = compute_wordpiece_score(freq_xy, freq_x, freq_y)
        scores[pair] = score
    if not scores:
        return None, 0
    best_pair = max(scores, key=scores.get)
    return best_pair, scores[best_pair]

def merge_pair_wordpiece(word_freq, pair_to_merge):
    """
    合并指定的字符对到新符号。

    参数：
        word_freq: List of tuples, 每个元组包含单词（列表形式）和其频次
        pair_to_merge: 要合并的字符对
    返回：
        更新后的单词频次列表
    """
    merged_word_freq = []
    new_symbol = create_new_symbol(pair_to_merge[0], pair_to_merge[1])
    for word, freq in word_freq:
        new_word = []
        i = 0
        while i < len(word):
            # 检查当前字符和下一个字符是否是要合并的字符对
            if (
                i < len(word) - 1
                and word[i] == pair_to_merge[0]
                and word[i + 1] == pair_to_merge[1]
            ):
                new_word.append(new_symbol)
                i += 2  # 跳过下一个字符，因为已合并
            else:
                new_word.append(word[i])
                i += 1
        merged_word_freq.append((new_word, freq))
    return merged_word_freq

def wordpiece_merge(word_freq, vocab_size):
    """
    执行 WordPiece 合并操作，直到词汇表达到预定大小。

    参数：
        word_freq: List of tuples, 每个元组包含单词（列表形式）和其频次
        vocab_size: 预定的词汇表大小
        
    返回：
        最终词汇表和合并记录
    """
    # 初始化词汇表
    vocab = set()
    for word, _ in word_freq:
        vocab.update(word)
    merges = []

    while len(vocab) < vocab_size:
        pair_freq, char_freq = count_char_pairs_wordpiece(word_freq)
        best_pair, best_score = find_best_pair_wordpiece(pair_freq, char_freq)
        if not best_pair:
            break
        # 合并最佳字符对
        new_symbol = create_new_symbol(best_pair[0], best_pair[1])
        word_freq = merge_pair_wordpiece(word_freq, best_pair)
        vocab.add(new_symbol)
        merges.append((best_pair, new_symbol))
        print(
            f"Merge: {best_pair} -> {new_symbol}, Score: {best_score:.4f}, 词汇表大小: {len(vocab)}"
        )

    return vocab, merges

# 示例
word_freq = [
    (['l', '##o', '##w'], 5),
    (['l', '##o', '##w', '##e', '##r'], 2),
    (['n', '##e', '##w', '##e', '##s', '##t'], 6),
    (['w', '##i', '##d', '##e', '##s', '##t'], 3)
]

# 预定词汇表大小为15
final_vocab_wp, merge_records_wp = wordpiece_merge(word_freq, 15)

print("\n最终词汇表 V:")
print(final_vocab_wp)

print("\n合并记录:")
for idx, (pair, new_sym) in enumerate(merge_records_wp, 1):
    print(f"Merge {idx}: {pair} -> {new_sym}")