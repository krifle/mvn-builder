# Maven Builder
This server will help to build maven project of desired git repository.

## 0. Run build server
```
$ mvn clean spring-boot:run
```

## 1. Get Info
You can get available java versions and maven versions  
These result values could be changed by editing `application.yaml`

### Request
```
http://localhost:8080/info
```

### Response Example
```json
{
  "javaHomeList": [
    {
      "version": "1.8",
      "home": "/dir/to/java/Contents/Home"
    },
    {
      "version": "11",
      "home": "/dir/to/java/Contents/Home"
    },
    {
      "version": "17",
      "home": "/dir/to/java/Contents/Home"
    }
  ],
  "mavenHomeList": [
    {
      "version": "3.6.3",
      "home": "/dir/to/maven/apache-maven-3.6.3"
    }
  ]
}
```

## 2. Build start
Requests for maven build.  
All parameters should be url encoded.

### Request Example
```
http://localhost:8080/start?
    source=https%3A%2F%2Fgithub.com%2Fkrifle%2Fmvn-builder.git&
    branch=master&
    buildOpt=clean%20package%20-DskipTests&
    targetPath=target%2Fmvn-builder-0.0.1-SNAPSHOT.jar
```

| Parameter    | Description                          | example                               |
|--------------|--------------------------------------|---------------------------------------|
| `source`     | Git address you want to clone        | https://github.com/krifle/mvn-builder |
| `branch`     | Desired git branch you want to build | master                                |
| `buildOpt`   | Maven build options                  | clean package -DskipTests             |
| `targetPath` | Result file you want to zip and send | target/mvn-builder-0.0.1-SNAPSHOT.jar |

### Response Example
Maven build will be started with `id` provided.  
Client must keep this `id` for later `/check` request.
```json
{
  "id": "6cd655f6-803c-42d6-a219-2768895716d7",
  "created": "2022-06-03T16:50:18.085094",
  "state": "START",
  "source": "https://github.com/krifle/mvn-builder",
  "buildOpt": "clean package -DskipTests",
  "outputBuffer": "",
  "resultUrl": ""
}
```

## 3. Build check
Client can frequently ask server for the build process of requested `id`.  
On this step, client can get the process logs from `outputBuffer` so you can keep track on it.  
`outputBuffer` is cleared once the client read it, so every time the client checks, it gets new logs.

### Request Example
```
http://localhot:8080/check?id=6cd655f6-803c-42d6-a219-2768895716d7
```

### Response Example
```json
{
  "id": "6cd655f6-803c-42d6-a219-2768895716d7",
  "created": "2022-06-03T16:50:18.084443",
  "state": "WORKING",
  "source": "https://github.com/krifle/mvn-builder",
  "buildOpt": "clean package -DskipTests",
  "outputBuffer": "/usr/bin/git clone -b master --single-branch https://github.com/krifle/mvn-builder /Users/user/Desktop/temp/mvn-builder/6cd655f6-803c-42d6-a219-2768895716d7/Cloning into '/Users/user/Desktop/temp/mvn-builder/6cd655f6-803c-42d6-a219-2768895716d7'...\nFFA01D819D7F/Users/user/apps/apache-maven-3.6.3/bin/mvn -f /Users/user/Desktop/temp/mvn-builder/6cd655f6-803c-42d6-a219-2768895716d7//pom.xml clean package -DskipTests[INFO] Scanning for projects...\n[INFO] \n[INFO] -------------------------< com.zh:mvn-builder >-------------------------\n[INFO] Building mvn-builder 0.0.1-SNAPSHOT\n[INFO] --------------------------------[ jar ]---------------------------------\n[INFO] \n[INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ mvn-builder ---\n[INFO] \n[INFO] --- maven-resources-plugin:3.2.0:resources (default-resources) @ mvn-builder ---\n[INFO] Using 'UTF-8' encoding to copy filtered resources.\n[INFO] Using 'UTF-8' encoding to copy filtered properties files.\n[INFO] Copying 1 resource\n[INFO] Copying 3 resources\n[INFO] \n[INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) @ mvn-builder ---\n[INFO] Nothing to compile - all classes are up to date\n[INFO] \n[INFO] --- kotlin-maven-plugin:1.6.10:compile (compile) @ mvn-builder ---\n[INFO] Applied plugin: 'spring'\n[WARNING] /Users/user/Desktop/temp/mvn-builder/6cd655f6-803c-42d6-a219-2768895716d7/src/main/kotlin/com/zh/mvn/builder/server/ProcessManager.kt: (116, 19) 'stop(): Unit' is deprecated. Deprecated in Java\n[WARNING] /Users/user/Desktop/temp/mvn-builder/6cd655f6-803c-42d6-a219-2768895716d7/src/main/kotlin/com/zh/mvn/builder/server/ProcessManager.kt: (117, 26) 'stop(): Unit' is deprecated. Deprecated in Java\n[WARNING] /Users/user/Desktop/temp/mvn-builder/6cd655f6-803c-42d6-a219-2768895716d7/src/main/kotlin/com/zh/mvn/builder/server/ServerInitializer.kt: (34, 34) Unnecessary non-null assertion (!!) on a non-null receiver of type String\n[WARNING] /Users/user/Desktop/temp/mvn-builder/6cd655f6-803c-42d6-a219-2768895716d7/src/main/kotlin/com/zh/mvn/builder/server/ServerInitializer.kt: (37, 36) Unnecessary non-null assertion (!!) on a non-null receiver of type String\n[INFO] \n[INFO] --- maven-resources-plugin:3.2.0:testResources (default-testResources) @ mvn-builder ---\n[INFO] Using 'UTF-8' encoding to copy filtered resources.\n[INFO] Using 'UTF-8' encoding to copy filtered properties files.\n[INFO] Copying 1 resource\n[INFO] \n[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ mvn-builder ---\n[INFO] Changes detected - recompiling the module!\n[INFO] \n[INFO] --- kotlin-maven-plugin:1.6.10:test-compile (test-compile) @ mvn-builder ---\n[INFO] Applied plugin: 'spring'\n",
  "resultUrl": ""
}
```

## 4. Force stop
You can force stop the build process of given `id`.

### Request Example
```
http://localhost:8080/stop?id=6cd655f6-803c-42d6-a219-2768895716d7
```


## etc.
### Upload Step
This project did not implement uploading process for security reason.  
If you need uploading process, you can fork this repository freely and implement for your own project.
