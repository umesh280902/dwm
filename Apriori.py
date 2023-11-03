class CombinationsGenerator:
    def __init__(self):
        self.result = []
        self.tmp = []

    def generate_combinations(self, max_number, k):
        self._generate_combinations_util(max_number, 0, k)

    def _generate_combinations_util(self, max_number, left, k):
        if k == 0:
            self.result.append(self.tmp[:])
            return
        for e in range(left, len(max_number)):
            self.tmp.append(max_number[e])
            self._generate_combinations_util(max_number, e + 1, k - 1)
            self.tmp.pop()

def print_table(table, freq):
    print('------------------')
    print("ItemSet\tFrequency")
    print('------------------')
    for index in range(len(freq)):
        print(f'{table[index]}\t== {freq[index]}')

n = int(input('Enter the number of transactions:'))
data_dict = {}
for i in range(n):
    data_dict[i] = [int(_) for _ in input(f'Enter the items of transactions:{i + 1}').split()]

support = int(input('Enter the minimum support:'))
min_confidence = float(input('Enter the minimum confidence (as a decimal):'))

def print_association_rule(frequent_list, freq, confidence_threshold):
    for index in range(len(frequent_list)):
        all_rules = []
        for size in range(len(frequent_list[index]) - 1):
            c = CombinationsGenerator()
            c.generate_combinations(frequent_list[index], size + 1)
            rules = c.result
            for group1 in rules:
                group2 = []
                for e in frequent_list[index]:
                    if e not in group1:
                        group2.append(e)
                count_group1 = 0
                count_group2 = 0
                count_both = 0
                for k in range(n):
                    if all(m in data_dict[k] for m in group1):
                        count_group1 += 1
                    if all(m in data_dict[k] for m in group2):
                        count_group2 += 1
                    if all(m in data_dict[k] for m in frequent_list[index]):
                        count_both += 1
                if count_group1 > 0:
                    confidence_rule = count_both / count_group1
                    if confidence_rule >= confidence_threshold:
                        all_rules.append((group1, group2, confidence_rule))
        print("\nAssociation rules with confidence >= {:.0%}:".format(confidence_threshold))
        for rule in all_rules:
            antecedent_str = ' ^ '.join(str(i) for i in rule[0])
            consequent_str = ' ^ '.join(str(i) for i in rule[1])
            print(f"{antecedent_str} => {consequent_str}, Confidence: {rule[2]:.2%}")

frequent_item_list = [k for k in set(j for i in data_dict.keys() for j in data_dict[i])]
item_size = 1
while True:
    print("\nIteration ", item_size, ":")
    combinations_generator = CombinationsGenerator()
    combinations_generator.generate_combinations(frequent_item_list, item_size)
    comb_item_set = combinations_generator.result
    current_frequent_item_list = []
    frequent = []
    for item_set in comb_item_set:
        cnt = 0
        for i in range(n):
            if all(item in data_dict[i] for item in item_set):
                cnt += 1
        if cnt >= support:
            current_frequent_item_list.append(item_set)
            frequent.append(cnt)
    print_table(current_frequent_item_list, frequent)
    print_association_rule(current_frequent_item_list, frequent, min_confidence)
    frequent_item_list = [k for k in set(j for i in current_frequent_item_list for j in i)]
    if item_size + 1 >= len(frequent_item_list):
        break
    item_size += 1
