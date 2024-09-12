## Generating a keystore
```sh
keytool -genkeypair \
  -alias huji \
  -keyalg RSA \
  -keysize 2048 \
  -storetype PKCS12 \
  -keystore huji.p12 \
  -validity 3650
```