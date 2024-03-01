import requests
import json

data = {
    "code": """
a = 0
b = 1
while a < 100:
    print(a)
    a, b = b, a + b
""",
    "language": "python"
}


headers = {
    "Content-Type": "application/json"
}

response = requests.post(url, data=json.dumps(data), headers=headers)

if response.status_code == 200:
    print("JSON data posted successfully.\n")
    response_data = response.json()
    print(response_data['output'])    

else:
    print(f"Failed to post JSON data. Status code: {response.status_code}")
    print(response.text)
