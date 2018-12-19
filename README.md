<h1>Retrofit Uploader</h1>
<hr>

<h2>Libraries used :</h2>
<ul>
  <li>Retrofit2</li>
  <li>nbsp-MaterialFilePicker</li>
 </ul>

<h2>Follow the steps to configure after cloning:</h2>
<ol>
  <li>
    <h2>Add the dependencies, under dependencies section of app gradle</h2>
   

    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:converter-scalars:2.3.0'
    implementation 'com.nbsp:library:1.09'      
    
    
  </li>
  <li>
    <h2>Add the url of the maven file on the file picker library, under android section of app gradle </h2>
    
    repositories {
        maven {
            url "http://dl.bintray.com/lukaville/maven"
        }
    }
    
  

</li>
  <li></li>
  <li></li>
