import time

import requests

url = "http://localhost:8080"
parameters = {
  'source': 'https://github.com/krifle/mvn-builder',
  'branch': 'master',
  'buildOpt': 'clean package -DskipTests',
  'targetPath': 'target/mvn-builder-0.0.1-SNAPSHOT.jar'
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
    print("ERROR or STOPPED")
    break

if downloadUrl != "":
  print("download " + downloadUrl)
