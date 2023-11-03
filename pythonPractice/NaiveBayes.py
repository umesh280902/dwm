def compute_probabilities(data):
    pass_count = sum(1 for row in data if row[-1] == "yes")
    fail_count = len(data) - pass_count
    p_pass = pass_count / len(data)
    p_fail = fail_count / len(data)
    return p_pass, p_fail

def test_classifier(data, test):
    p_pass, p_fail = compute_probabilities(data)
    for row in data:
        if row[:-1] == test:
            if row[-1] == "yes":
                p_pass *= pass_count / len(data)
            else:
                p_fail *= fail_count / len(data)
    p_pass /= (p_pass + p_fail)
    p_fail /= (p_pass + p_fail)
    prediction = "yes" if p_pass > p_fail else "no"
    return prediction, p_pass, p_fail

data = [
    ["<=30","high","no","fair","no"],
    ["<=30","high","no","excellent","no"],
    ["31...40","high","no","fair","yes"],
    [">40","medium","no","fair","yes"],
    [">40","low","yes","fair","yes"],
    [">40","low","yes","excellent","no"],
    ["31...40","low","yes","excellent","yes"],
    ["<=30","medium","no","fair","no"],
    ["<=30","low","yes","fair","yes"],
    [">40","medium","yes","fair","yes"],
    ["<=30","medium","yes","excellent","yes"],
    ["31...40","medium","no","excellent","yes"],
    ["31...40","high","yes","fair","yes"],
    [">40","medium","no","excellent","no"]
]

test = ["<=30", "high", "no", "fair"]  # Replace with your test data
prediction, p_pass, p_fail = test_classifier(data, test)

print("Test Data:", test)
print("Prediction:", prediction)
print("Probability of 'yes':", p_pass)
print("Probability of 'no':", p_fail)
