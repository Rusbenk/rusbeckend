use actix_web::{App, get, HttpResponse, HttpServer, Responder};
use actix_web::http::StatusCode;
use serde::Serialize;



#[get("/greet")]
async fn greet() -> impl Responder {
    #[derive(Serialize)]
    struct Response {
        hello: String,
    }

    let res = Response{ hello: "world".to_string() };

    HttpResponse::build(StatusCode::CREATED).json(res)
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    HttpServer::new(|| {
        App::new()
            .service(greet)
    })
        .bind(("0.0.0.0", 3000))?
        .run()
        .await
}
