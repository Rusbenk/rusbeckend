use std::collections::HashMap;
use warp::Filter;

async fn hello_fn(name: String) ->Result< impl warp::Reply, warp::Rejection> {
    let mut response: HashMap<String,String> = HashMap::new();

    response.insert(String::from("Hello"), String::from(name));

    Ok(warp::reply::json(&response))
}

#[tokio::main]
async fn main() {
    // GET /hello/warp => 200 OK with body "Hello, warp!"
    let hello = warp::get()
        .and(warp::path("hello"))
        .and(warp::path::param())
        .and(warp::path::end())
        .and_then(hello_fn);

    warp::serve(hello)
        .run(([0, 0, 0, 0], 3030))
        .await;

}