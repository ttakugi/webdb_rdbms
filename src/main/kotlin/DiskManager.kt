import java.io.File
import java.io.RandomAccessFile




/**
 * 採番ページ ID
 */
data class PageId(val value: Long)

/**
 * ファイルバス
 */
data class PathName(val value: String)

/**
 * ディスクへの読み書きを担うクラス
 */
class DiskManager private constructor(

    /**
     * ヒープファイルのファイルディスクリプタ
     */
    var heapFile: File,

    /**
     * 採番するページ ID を決めるカウンタ
     */
    var nextPageId: PageId
) {
    companion object {

        /**
         * ページサイズ
         */
        private const val PAGE_SIZE: Long = 4096

        /**
         * ディスクマネージャを作成する
         */
        fun create(file: File): DiskManager {
            val heapFileSize = file.length()
            val nextPageId = heapFileSize / PAGE_SIZE
            return DiskManager(
                heapFile = file,
                nextPageId = PageId(nextPageId)
            )
        }

        /**
         * ファイルバスを指定して開く
         * 存在しない場合は null を返す
         */
        fun open(pathName: PathName): DiskManager? {
            val file = File(pathName.value)
            return if (file.exists()) create(file) else null
        }
    }

    /**
     * 新しいページ ID を採番する
     */
    fun allocatePage(): PageId {
        val pageId = this.nextPageId
        this.nextPageId = PageId(pageId.value + 1)
        return pageId
    }

    /**
     * ページのデータを読み出す
     */
    fun readPageData(pageId: PageId): String {
        val offset = PAGE_SIZE * pageId.value
        val accessFile = RandomAccessFile(this.heapFile, "rw")
        accessFile.seek(offset)
        var file: String? = ""
        var files = mutableListOf<String>()
        while (file != null) {
            file = accessFile.readLine()
            if (file != null) {
                files = files.plus(file).toMutableList()
            }
        }
        return files.joinToString("\n")
    }

    /**
     * データをページに書き出す
     */
    fun writePageData(pageId: PageId, data: String) {
        val offset = PAGE_SIZE * pageId.value
        val accessFile = RandomAccessFile(this.heapFile, "rw")
        accessFile.seek(offset)
        accessFile.writeBytes(data)
    }
}
