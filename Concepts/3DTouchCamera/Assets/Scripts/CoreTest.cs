using UnityEngine;

public class CoreTest : MonoBehaviour {
	CameraController controller;
	public GameObject pivotObject;

	public Material outlineMaterial;
	public Material defaultMaterial;
	void Start() {
		controller = Camera.main.GetComponent<CameraController>();
		controller.OnTapped += OnTap;

		controller.pivotObject = pivotObject;
	}

	void OnTap(Vector2 mousePosition) {
		Debug.Log("Tap");
		Ray ray = Camera.main.ScreenPointToRay(mousePosition);
		RaycastHit hit;
		if(Physics.Raycast(ray, out hit)) {
			if (pivotObject != null)
			{
				pivotObject.layer = LayerMask.NameToLayer("Default");
			}
			pivotObject = hit.collider.gameObject;
			controller.pivotObject = pivotObject;

			pivotObject.layer = LayerMask.NameToLayer("Outline");
		}
	}
}