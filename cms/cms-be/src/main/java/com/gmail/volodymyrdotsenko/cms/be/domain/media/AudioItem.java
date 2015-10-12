package com.gmail.volodymyrdotsenko.cms.be.domain.media;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;

import com.gmail.volodymyrdotsenko.cms.be.domain.local.Language;

@Entity
@Table(name = "MEDIA_ITEM_AUDIOS")
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class AudioItem extends MediaItem {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Column(name = "NAME", length = 255)
	private String name;

	@Column(name = "LENGTH")
	private Long length;

	@Column(name = "TRACK", length = 255)
	private String track;

	@Column(name = "ARTIST", length = 255)
	private String artist;

	@Column(name = "TITLE", length = 255)
	private String title;

	@Column(name = "ALBUM", length = 255)
	private String album;

	@Column(name = "YEAR", length = 10)
	private String year;

	@Column(name = "GENRE")
	private Integer genre;

	@Column(name = "GENRE_DESCRIPTION", length = 255)
	private String genreDescription;

	@Column(name = "COMMENT", length = 255)
	private String comment;

	@Column(name = "FILE_NAME", length = 255)
	private String fileName;

	@Column(name = "MIME_TYPE", length = 255)
	private String MIMEType;

	@Column(name = "FILE_LENGTH")
	private Long fileLength;

	@ElementCollection
	@CollectionTable(name = "MEDIA_ITEM_TEXTS", joinColumns = {
			@javax.persistence.JoinColumn(name = "REF_ITEM_ID", referencedColumnName = "ID") })
	@MapKeyJoinColumn(name = "CODE")
	private Map<Language, TextItem> textItem = new HashMap<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Integer getGenre() {
		return genre;
	}

	public void setGenre(Integer genre) {
		this.genre = genre;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getGenreDescription() {
		return genreDescription;
	}

	public void setGenreDescription(String genreDescription) {
		this.genreDescription = genreDescription;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMIMEType() {
		return MIMEType;
	}

	public void setMIMEType(String mIMEType) {
		MIMEType = mIMEType;
	}

	public Long getFileLength() {
		return fileLength;
	}

	public void setFileLength(Long fileLength) {
		this.fileLength = fileLength;
	}

	public Map<Language, TextItem> getTextItem() {
		return textItem;
	}

	public void setTextItem(Map<Language, TextItem> textItem) {
		this.textItem = textItem;
	}

	@Override
	public String toString() {
		return "AudioItem [name=" + name + ", length=" + length + ", track=" + track + ", artist=" + artist + ", title="
				+ title + ", album=" + album + ", year=" + year + ", genre=" + genre + ", genreDescription="
				+ genreDescription + ", comment=" + comment + "]";
	}
}