import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//filter data sob by category
class planViewModel : ViewModel() {
    private val db = Firebase.firestore


    private val _tempatList = MutableStateFlow<List<Tempat>>(emptyList())
    val tempatList: StateFlow<List<Tempat>> get() = _tempatList

    init {
        getTempatFilter("All")
    }


    fun getTempatFilter(category: String) {
        val query = if (category == "All") db.collection("tempat") else db.collection("tempat").whereEqualTo("Category", category)
        query.get().addOnSuccessListener {
            snapshot -> _tempatList.value = snapshot.toObjects(Tempat::class.java)

            viewModelScope.launch {
                val tempatData = snapshot?.map {
                    queryDocumentSnapshot -> Tempat(
                        id = queryDocumentSnapshot.id,
                        address = queryDocumentSnapshot.getString("Addres") ?: "",
                        category = queryDocumentSnapshot.getString("Category") ?: "",
                        close = queryDocumentSnapshot.getString("Close") ?: "",
                        deskripsi = queryDocumentSnapshot.getString("Deskripsi") ?: "",
                        open = queryDocumentSnapshot.getString("Open") ?: "",
                        phoneNumber = queryDocumentSnapshot.getString("Phone Number") ?: "",
                        priceRange = queryDocumentSnapshot.getLong("Price Range") ?: 0,
                        tags = queryDocumentSnapshot.get("Tag") as? List<Long> ?: emptyList()
                    )
                } ?: emptyList()
                _tempatList.value = tempatData
            }
        }

    }
}
