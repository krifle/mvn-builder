import requests
import sys
import time

url = sys.argv[1]
parameters = {
  'source': sys.argv[2],
  'branch': sys.argv[3],
  'buildOpt': sys.argv[4],
  'targetPath': sys.argv[5]
}

print(url + "/start")
print(parameters)

buildInfo = requests.get(url + "/start", params=parameters).json()
print(buildInfo)

downloadUrl = ""

while 1:
  time.sleep(1)
  checkInfo = requests.get(url + "/check?id=" + buildInfo["id"]).json()
  outputBuffer = checkInfo["outputBuffer"].strip()
  if outputBuffer:
    print(outputBuffer)
  if checkInfo["state"] == "DONE":
    downloadUrl = checkInfo["resultUrl"]
    break
  if checkInfo["state"] == "STOPPED" or checkInfo["state"] == "ERROR":
    break

if downloadUrl != "":
  print("download " + downloadUrl)
