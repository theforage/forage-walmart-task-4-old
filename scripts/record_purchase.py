import json

import requests

#########################################################################################
# When executed, this script hits the /record_purchase endpoint and prints the response #
# Configure parameters by adjusting the constants below                                 #
#########################################################################################

FIRST_NAME = "Finn"
LAST_NAME = "Jakes"
EMAIL = "finn.jakes@gmail.com"

# purchase date must be in the format YYYY-MM-DD
PURCHASE_DATE = "2021-09-02"

# vr headset name must be one of [MagicFace3000, NogginWobbler, Visionmax, Thirdeye,
#                                 SkullShaker, Wiggleroom, ExtraDimensionalBrainWarbler]
VR_HEADSET_NAME = "NogginWobbler"


def collect_purchase_info():
    data = {
        "customerFirstName": FIRST_NAME,
        "customerLastName": LAST_NAME,
        "customerEmail": EMAIL,
        "purchaseDate": PURCHASE_DATE,
        "vrHeadsetName": VR_HEADSET_NAME
    }
    response = requests.post("http://0.0.0.0:7000/record_purchase", json=data)
    if response.status_code != 200:
        print("an error occurred:")
        if response.status_code == 400:
            print(response.content.decode("utf-8"))
        elif response.status_code == 500:
            print("an internal server error occurred, check the server logs")
        else:
            print(f"unknown status code: {response.status_code}, check the server logs")
    else:
        print("recorded new purchase:")
        print(json.dumps(data, indent=4))


if __name__ == '__main__':
    collect_purchase_info()
