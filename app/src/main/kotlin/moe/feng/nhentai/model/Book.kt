package moe.feng.nhentai.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import moe.feng.nhentai.R

@Entity(tableName = Book.TAG) class Book: History.KeyContainer<Book.HistoryKey> {

	// Serializable fields
	@PrimaryKey @SerializedName("id") lateinit var bookId: String
	@SerializedName("media_id") lateinit var galleryId: String
	private @SerializedName("title") var titles: BookTitle = BookTitle()
	@SerializedName("upload_date") var uploadDate: Long = 0L
	@SerializedName("num_favorites") var favCount: Int = 0
	var scanlator: String = ""
	@SerializedName("num_pages") var pageCount: Int = 0
	var images: BookImages = BookImages()
	@SerializedName("tags") var allTags: Array<Tag> = emptyArray()

	// Favourite mark
	var isFavourite: Boolean = false

	// Methods
	val language: String get() = allTags.find {
		it.type == Tag.TYPE_LANGUAGE && it.name != Tag.LANG_TRANSLATED }?.name ?: Tag.LANG_JAPANESE
	val tags: Array<Tag> get() = allTags.filter { it.type == Tag.TYPE_TAG }.toTypedArray()
	val categories: Array<Tag> get() = allTags.filter { it.type == Tag.TYPE_CATEGORY }.toTypedArray()
	val characters: Array<Tag> get() = allTags.filter { it.type == Tag.TYPE_CHARACTER }.toTypedArray()
	val groups: Array<Tag> get() = allTags.filter { it.type == Tag.TYPE_GROUP }.toTypedArray()
	val parodies: Array<Tag> get() = allTags.filter { it.type == Tag.TYPE_PARODY }.toTypedArray()
	val artists: Array<Tag> get() = allTags.filter { it.type == Tag.TYPE_ARTIST }.toTypedArray()

	val title: String get() = titles.english
	val jpTitle: String? get() = titles.japanese
	val prettyTitle: String? get() = titles.pretty

	fun getLanguageFlagRes(): Int = when (language) {
		Tag.LANG_CHINESE -> R.drawable.ic_lang_cn
		Tag.LANG_ENGLISH -> R.drawable.ic_lang_gb
		Tag.LANG_JAPANESE -> R.drawable.ic_lang_jp
		else -> 0
	}

	override fun getHistoryAction(): Int = History.ACTION_READ_BOOK

	override fun getHistoryKey(): Book.HistoryKey
			= HistoryKey(bookId, galleryId, titles, images.cover)

	data class BookTitle(
			var japanese: String? = null,
			var pretty: String? = null,
			var english: String = ""
	)

	class BookImages(
			var cover: Picture? = null,
	        var pages: Array<Picture> = emptyArray(),
	        var thumbnail: Picture? = null
	)

	class HistoryKey(
			override val id: String,
	        val galleryId: String,
	        val titles: BookTitle,
	        val cover: Picture? = null
	): History.Key

	companion object {

		const val TAG = "Book"

	}

}