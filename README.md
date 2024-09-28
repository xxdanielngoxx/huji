# Huji
The Huji is a web-based application that aims to help users manage these "hụi" games.

For more information the "hụi" game: [WIKI HUI](https://vi.wikipedia.org/wiki/Ch%C6%A1i_h%E1%BB%A5i)

## How do I run the whole application?
### Build and sync the web application
```sh
cd web
./sync_to_backend.sh
```

### Run the API application
#### Spin up the dependencies
```sh
cd devtools
docker compose up
```

#### Run the API application
```sh
cd api
./gradlew bootRun
```
