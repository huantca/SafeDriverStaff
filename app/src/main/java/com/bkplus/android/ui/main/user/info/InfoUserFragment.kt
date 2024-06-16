package com.bkplus.android.ui.main.user.info

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bkplus.android.SharedViewModel
import com.bkplus.android.common.BaseFragment
import com.bkplus.android.common.BasePrefers
import com.bkplus.android.model.User
import com.bkplus.android.ultis.Constants
import com.bkplus.android.ultis.gone
import com.bkplus.android.ultis.loadImage
import com.harrison.myapplication.R
import com.harrison.myapplication.databinding.FragmentInfoUserBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class InfoUserFragment : BaseFragment<FragmentInfoUserBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_info_user
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun setupData() {
        super.setupData()
        val dataType = resources.getStringArray(R.array.typeCar)
        val dataCar = resources.getStringArray(R.array.car)
        val adapterType = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item, dataType
            )
        }
        val adapterCar = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item, dataCar
            )
        }
        binding.spinnerType.adapter = adapterType
        binding.spinnerCar.adapter = adapterCar

        binding.spinnerType.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                BasePrefers.getPrefsInstance().rangeOfVehicleUser = dataType[position]
                binding.tvTypeVehicle.text = BasePrefers.getPrefsInstance().rangeOfVehicleUser
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.spinnerCar.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                BasePrefers.getPrefsInstance().vehicleModelUser = dataCar[position]
                binding.tvNameCar.text = BasePrefers.getPrefsInstance().vehicleModelUser
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    override fun setupUI() {
        super.setupUI()
        binding.apply {
            val info = BasePrefers.getPrefsInstance().infoUser
            binding.tvName.text = info?.name
            binding.tvPhone.text = info?.phone
            if (info?.avatar != null) {
                binding.imgAvatar.loadImage(Constants.BASE_URL_IMAGE + info?.avatar)
            }
            BasePrefers.getPrefsInstance().vehicleModelUser?.let {
                binding.tvNameCar.text = it
            }
            BasePrefers.getPrefsInstance().rangeOfVehicleUser?.let {
                binding.tvTypeVehicle.text = it
            }
        }
    }

    private fun updateAvatar(uri: Uri) {
        val file = getFile(requireContext(), uri)
        val requestFile: RequestBody =
            RequestBody.create(MultipartBody.FORM, file)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", file.getName(), requestFile)
        val user = BasePrefers.getPrefsInstance().infoUser ?: return
        sharedViewModel.updateAvatar(body, buildPostBody(user), {
//            if (this@InfoUserFragment.isAdded) {
//                context?.getString(R.string.saved_successfully)?.let { toast(it) }
//            }
        }, {
//            if (this@InfoUserFragment.isAdded) {
//                context?.getString(R.string.saved_fail)?.let { toast(it) }
//            }
        })
    }

    private fun buildPostBody(user: User): RequestBody {
        // build body
        val jsonObject = JSONObject()
        jsonObject.put("id", user.id)
        return jsonObject.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }

    fun getFile(context: Context, uri: Uri): File {
        val destinationFilename =
            File(context.filesDir.path + File.separatorChar + queryName(context, uri))
        try {
            context.contentResolver.openInputStream(uri).use { ins ->
                if (ins != null) {
                    createFileFromStream(ins, destinationFilename)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return destinationFilename
    }

    fun createFileFromStream(ins: InputStream, destination: File?) {
        try {
            FileOutputStream(destination).use { os ->
                val buffer = ByteArray(4096)
                var length: Int
                while (ins.read(buffer).also { length = it } > 0) {
                    os.write(buffer, 0, length)
                }
                os.flush()
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    private fun queryName(context: Context, uri: Uri): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)!!
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }

    override fun setupListener() {
        super.setupListener()


        binding.apply {
            icBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ctlAvatar.setOnClickListener {
                permissionGallery()
            }
            imgSave.setOnClickListener {
                imgSave.gone()
                val user = BasePrefers.getPrefsInstance().infoUser ?: return@setOnClickListener
                user.name = binding.tvName.text.toString()
                user.phone = binding.tvPhone.text.toString()
                sharedViewModel.saveInfoUser(
                    user, {
                        if (this@InfoUserFragment.isAdded) {
                            toast(getString(R.string.saved_successfully))
                        }
                    }, {
                        if (this@InfoUserFragment.isAdded) {
                            toast(getString(R.string.saved_fail))
                        }
                    }
                )
            }

            tvName.setOnFocusChangeListener { v, hasFocus ->
                imgSave.visibility = View.VISIBLE
            }
            tvPhone.setOnFocusChangeListener { v, hasFocus ->
                imgSave.visibility = View.VISIBLE
            }
        }
    }

    private val checkPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                takePhoto()
            }
        }

    private fun takePhoto() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, 10)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10) {
            val selectedImageUri = data?.data
            if (null != selectedImageUri) {
                updateAvatar(selectedImageUri)
                binding.imgAvatar.setImageURI(selectedImageUri)
            }
        }
    }

    private fun permissionGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + context?.packageName)
                startActivity(intent)
            } else {
                checkPermission.launch(
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            }
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + context?.packageName)
                startActivity(intent)
            } else {
                checkPermission.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }
}