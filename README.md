A sample UI framework for Android
=================================


Using
=====

```xml
<?xml version="1.0" encoding="utf-8"?>

<com.nicaiya.canvaslayout.library.LinearElement
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <com.nicaiya.canvaslayout.library.TextElement
        android:id="@+id/linear_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="LinearLayout"
        android:textColor="@android:color/black"
        android:textSize="20sp" />

    <com.nicaiya.canvaslayout.library.LinearElement
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="5dp"
        android:orientation="horizontal">

        <com.nicaiya.canvaslayout.library.ImageElement
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:scaleType="fitStart"
            android:src="@mipmap/tweet_retweet" />

        <com.nicaiya.canvaslayout.library.ImageElement
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="10dp"
            android:scaleType="fitStart"
            android:src="@mipmap/tweet_favourite" />

        <com.nicaiya.canvaslayout.library.ImageElement
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="10dp"
            android:scaleType="fitStart"
            android:src="@mipmap/tweet_reply" />

    </com.nicaiya.canvaslayout.library.LinearElement>

    <com.nicaiya.canvaslayout.library.TextElement
        android:id="@+id/absolute_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="Absolute"
        android:textColor="@android:color/black"
        android:textSize="20sp" />


    <com.nicaiya.canvaslayout.library.AbsoluteElement
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="5dp">

        <com.nicaiya.canvaslayout.library.TextElement
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_x="25dp"
            android:layout_y="25dp"
            android:text="text1"
            android:textColor="@android:color/black"
            android:textSize="25sp" />

        <com.nicaiya.canvaslayout.library.TextElement
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_x="10dp"
            android:layout_y="55dp"
            android:text="text2"
            android:textColor="@android:color/black"
            android:textSize="20sp" />

    </com.nicaiya.canvaslayout.library.AbsoluteElement>

    <com.nicaiya.canvaslayout.library.TextElement
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="FrameLayout"
        android:textColor="@android:color/black"
        android:textSize="20sp" />

    <com.nicaiya.canvaslayout.library.FrameElement
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="5dp">

        <com.nicaiya.canvaslayout.library.TextElement
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="text1"
            android:textColor="@android:color/black"
            android:textSize="10sp" />

        <com.nicaiya.canvaslayout.library.TextElement
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="text2"
            android:textColor="@android:color/black"
            android:textSize="25sp" />

    </com.nicaiya.canvaslayout.library.FrameElement>

</com.nicaiya.canvaslayout.library.LinearElement>
```

