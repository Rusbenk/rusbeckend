#![allow(opaque_hidden_inferred_bound)]

use std::collections::HashMap;
use warp::Filter;

async fn hello_handler(name: String) ->Result<impl warp::Reply, warp::Rejection> {
    let mut response: HashMap<String,String> = HashMap::new();

    response.insert(String::from("Hello"), String::from(name));

    Ok(warp::reply::json(&response))
}

pub fn hello() -> impl Filter<Extract = impl warp::Reply, Error = warp::Rejection> + Clone {
    warp::path("hello")
        .and(warp::path::param())
        .and(warp::path::end())
        .and_then(hello_handler)

}

pub fn hello2() -> impl Filter<Extract = impl warp::Reply, Error = warp::Rejection> + Clone {
    warp::path("hello2")
        .and(warp::path("world"))
        .and(warp::path::param())
        .and(warp::path::end())
        .and_then(hello_handler)
}
