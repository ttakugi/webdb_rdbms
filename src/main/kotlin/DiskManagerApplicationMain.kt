fun main() {
    val pathName = PathName("") // path を入力する
    val manager = DiskManager.open(pathName)
    if (manager == null) {
        return
    }
    val pageId = PageId(1)
    println(manager.readPageData(pageId))

    manager.writePageData(pageId, "fugahoge")
    println(manager.readPageData(pageId))

    manager.writePageData(pageId, "fuga\nhoge")
    println(manager.readPageData(pageId))
}
