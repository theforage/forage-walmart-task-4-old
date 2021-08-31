import json

import requests

###############################################################################################
# When executed, this script hits the /collect_purchase_info endpoint and prints the response #
# Configure parameters by adjusting the constants below                                       #
###############################################################################################


PURCHASE_ID = 1


def collect_purchase_info():
    data = {
        "purchaseId": PURCHASE_ID
    }
    response = requests.post("http://0.0.0.0:7000/collect_purchase_info", json=data)
    if response.status_code != 200:
        print("an error occurred:")
        if response.status_code == 400:
            print(response.content.decode("utf-8"))
        elif response.status_code == 500:
            print("an internal server error occurred, check the server logs")
        else:
            print(f"unknown status code: {response.status_code}, check the server logs")
    else:
        print(f"purchase info for purchase_id: {PURCHASE_ID}")
        print(json.dumps(response.json(), indent=4))


if __name__ == '__main__':
    collect_purchase_info()
