import Foundation

@objc public class SmsApi: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
