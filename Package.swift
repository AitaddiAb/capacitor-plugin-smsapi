// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorPluginSmsapi",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorPluginSmsapi",
            targets: ["SmsApiPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "SmsApiPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/SmsApiPlugin"),
        .testTarget(
            name: "SmsApiPluginTests",
            dependencies: ["SmsApiPlugin"],
            path: "ios/Tests/SmsApiPluginTests")
    ]
)
