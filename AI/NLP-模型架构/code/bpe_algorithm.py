from collections import defaultdict

class BPETokenizer():
    def __init__(self):
        self.merge = {}
        self.id_2_char = {}
        self.char_2_id = {}

    def train(self, input_texts, vacab_size):
        """
        BPE 算法的训练过程

        :param input_texts: 输入语料库
        :param vacab_size: 目标词汇表大小
        :return:
        """
        # 1. 对输入预料进行七味粉
        unique_chars = list(set(list(input_texts)))

        # 2. 得到一个初始化的字段
        id_2_char = {idx: char for idx, char in enumerate(unique_chars)}
        char_2_id = {char: idx for idx, char in enumerate(unique_chars)}

        # 3. 利用字段对输入预料进行 id 化
        ids = [char_2_id[c] for c in input_texts]

        merge_times = vacab_size - len(unique_chars)
        vacab_size = len(unique_chars) - 1
        merge = {}
        # 4. 训练、合并子词，知道字典的大小达到目标词汇表大小
        for i in range(merge_times):
            if len(ids) == 1:
                break
            # 统计相邻子词出现的频率
            stats = self.stats(ids)

            # 找出出现频率最高的相邻子词对
            pair = max(stats, key=stats.get)
            vacab_size += 1
            id_2_char[vacab_size] = id_2_char[pair[0]] + id_2_char[pair[1]]
            char_2_id[id_2_char[pair[0]] + id_2_char[pair[1]]] = vacab_size
            merge[pair] = vacab_size

            # 根据当前的词典，合并 ids
            ids = self.merge_ids(ids, pair, vacab_size)
        self.merge = merge
        self.char_2_id = char_2_id
        self.id_2_char = id_2_char

    def stats(self, ids):
        """
        统计相邻子词出现的批量
        :param ids: 根据当前词典索引话后的语料库
        :return:
        """
        count = {}
        for item in zip(ids[:-1], ids[1:]):
            count[item] = count.get(item, 0) + 1
        return count

    def merge_ids(self, ids, pair, idx):
        """
        合并语料库里面的相邻子词对，并更新 ids

        :param ids: 语料库未更新前的 ids
        :param pair: 当前待合并的相邻子词对
        :param idx: 当前合并的相邻子词对在词典里面的 id
        :return:
        """
        new_ids = []
        i = 0;
        while i < len(ids):
            if ids[i] == pair[0] and ids[i + 1] == pair[1] and i < len(ids) - 1:
                new_ids.append(idx)
                i += 2
            else:
                new_ids.append(ids[i])
                i += 1
        return new_ids

    def encode(self, text):
        """
        对文本进行编码，返回编码后的 ids

        :param text: 输入文本
        :return: 编码后的 ids
        """
        ids = [self.char_2_id[c] for c in text]
        print(ids)

        while len(ids) >= 2:
            stats = self.stats(ids)
            print(f"stats: {stats}")
            pair = min(stats, key = lambda p: self.merge.get(p, float("inf")))
            print(f"pair: {pair}")

            if pair not in self.merge:
                break
            ids = self.merge_ids(ids, pair, self.merge[pair])
            print(f"ids: {ids}")
        return ids


    def decode(self, ids):
        """
        对 ids 进行解码，返回解码后的文本

        :param ids: 输入的 ids
        :return: 解码后的文本
        :return:
        """
        return "".join([self.id_2_char[idx] for idx in ids])

t1 = BPETokenizer()
train_text = """
    Hello, this is a training text. The tokenizer will split the text into words and assign an id
    to each word. This is a fantastic world!.
    """
t1.train(input_texts=train_text, vacab_size=48)
print(t1.id_2_char)

res = t1.encode("Hello, world!")
print(f"encoded ids: {res}")
print(f"decoded text: {t1.decode(res)}")


"""
BPE（Byte Pair Encoding）算法是一种基于统计的子词分割方法，常用于自然语言处理中的文本编码。BPE 的核心思想是通过迭代地合并频率最高的相邻字符对来构建一个词汇表，从而将文本分割成更小的单元（子词）。以下是 BPE 算法的主要步骤：
"""
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
