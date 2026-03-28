from collections import defaultdict

def count_char_pairs(word_freq):
    """
    计算字符对的频次。
    
    参数：
        word_freq: List of tuples, 每个元组包含单词（列表形式）和其频次
    
    返回：
        字符对频次的字典
    """
    pair_freq = defaultdict(int)
    for word, freq in word_freq:
        for i in range(len(word) - 1):
            pair = (word[i], word[i + 1])
            pair_freq[pair] += freq
    return pair_freq

def find_best_pair(freq):
    """
    找到频次最高的字符对。
    
    参数：
        freq: 字符对频次的字典
        
    返回：
        频次最高的字符对及其频次
    """
    if not freq:
        return None, 0
    best_pair = max(freq, key=freq.get)
    return best_pair, freq[best_pair]

def merge_pair(word_freq, pair_to_merge):
    """
    合并指定的字符对到新符号。
    
    参数：
        word_freq: List of tuples, 每个元组包含单词（列表形式）和其频次
        pair_to_merge: 要合并的字符对
    
    返回：
        更新后的单词频次列表
    """
    merged_word_freq = []
    pair_str = ''.join(pair_to_merge)
    for word, freq in word_freq:
        new_word = []
        i = 0
        while i < len(word):
            # 检查当前字符和下一个字符是否是要合并的字符对
            if i < len(word) - 1 and word[i] == pair_to_merge[0] and word[i + 1] == pair_to_merge[1]:
                new_word.append(pair_str)
                i += 2  # 跳过下一个字符，因为已合并
            else:
                new_word.append(word[i])
                i += 1
        merged_word_freq.append((new_word, freq))
    return merged_word_freq

def bpe_merge(word_freq, vocab_size):
    """
    执行 BPE 合并操作，直到词汇表达到预定大小。
    
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
        pair_freq = count_char_pairs(word_freq)
        best_pair, best_freq = find_best_pair(pair_freq)
        if not best_pair:
            break
        # 合并最佳字符对
        word_freq = merge_pair(word_freq, best_pair)
        new_symbol = ''.join(best_pair)
        vocab.add(new_symbol)
        merges.append((best_pair, new_symbol))
        print(f"Merge: {best_pair} -> {new_symbol}, 词汇表大小: {len(vocab)}")
            
    return vocab, merges

# 示例
word_freq = [
    (['l', 'o', 'w'], 5),
    (['l', 'o', 'w', 'e', 'r'], 2),
    (['n', 'e', 'w', 'e', 's', 't'], 6),
    (['w', 'i', 'd', 'e', 's', 't'], 3)
]

# 预定词汇表大小为13
final_vocab, merge_records = bpe_merge(word_freq, 13)

print("\n最终词汇表 V:")
print(final_vocab)

print("\n合并记录:")
for idx, (pair, new_sym) in enumerate(merge_records, 1):
    print(f"Merge {idx}: {pair} -> {new_sym}")
