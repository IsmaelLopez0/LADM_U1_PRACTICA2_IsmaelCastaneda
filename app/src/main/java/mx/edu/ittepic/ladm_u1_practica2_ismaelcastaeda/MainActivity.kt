package mx.edu.ittepic.ladm_u1_practica2_ismaelcastaeda

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            if (radioButton.isChecked() ) {
                guardarInterna()
                return@setOnClickListener
            }
            if (radioButton2.isChecked() ) {
                if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  PackageManager.PERMISSION_DENIED){
                    //Solicita permisos
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        0)
                }
                if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  PackageManager.PERMISSION_GRANTED) {
                    guardarExterna()
                }else{
                    mensaje("Necesitas dar permisos", "Error")
                }
                return@setOnClickListener
            }
            mensaje("Selecciona uno","Advertencia");
        }

        button2.setOnClickListener {
            if (radioButton.isChecked() ) {
                abrirInterna()
                return@setOnClickListener
            }
            if (radioButton2.isChecked() ) {
                if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  PackageManager.PERMISSION_DENIED){
                    //Solicita permisos
                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        0)
                }
                if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  PackageManager.PERMISSION_GRANTED) {
                    abrirExterna()
                }else{
                    mensaje("Necesitas dar permisos", "Error")
                }
                return@setOnClickListener
            }
            mensaje("Selecciona uno","Advertencia");
        }
    }

    fun abrirExterna() {
        if(noSD()){
            mensaje("No hay memoria externa", "Error")
            return
        }
        var rutaSD = Environment.getExternalStorageDirectory()
        var datosArchivo = File(rutaSD.absolutePath, editText2.text.toString())
        try {
            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            var allText = flujoEntrada.use(BufferedReader::readText)
            editText.setText(allText)
            flujoEntrada.close()
        }catch ( error : IOException ){
            mensaje(error.message.toString(), "Error")
        }
    }

    fun noSD():Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

    fun guardarExterna() {
        if(noSD()){
            mensaje("No hay memoria externa", "Error")
            return
        }
        var rutaSD = Environment.getExternalStorageDirectory()
        var datosArchivo = File(rutaSD.absolutePath, editText2.text.toString())
        try {
            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data = editText.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Se guardó correctamene","EXITO!")
            editText.setText("")
        }catch ( error : IOException ){//Error de lectura o escritura
            mensaje(error.message.toString(), "Error")
        }
    }

    fun guardarInterna(){
        try{
            var flujoSalida = OutputStreamWriter(openFileOutput(editText2.text.toString(), Context.MODE_PRIVATE))
            flujoSalida.write(editText.text.toString())
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Se guardó correctamene", "Exito")
            editText.setText("")
        }catch (error : IOException){
            mensaje(error.message.toString(), "Error")
        }
    }

    fun abrirInterna(){
        try {
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(editText2.text.toString())))
            var allText = flujoEntrada.use(BufferedReader::readText)
            editText.setText(allText)
            flujoEntrada.close()
        }catch ( error : IOException ){
            mensaje(error.message.toString(), "Error")
        }
    }

    fun mensaje(m : String, t : String){
        AlertDialog.Builder(this)
            .setMessage(m)
            .setTitle(t)
            .setPositiveButton("Ok"){ d,i -> }
            .show()
    }
}
