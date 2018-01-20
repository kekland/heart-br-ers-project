using UnityEngine;

public class CoreTest : MonoBehaviour {
	CameraController controller;

	void Start() {
		controller = Camera.main.GetComponent<CameraController>();
		controller.OnTapped += OnTap;
	}

	void OnTap(Vector2 mousePosition) {
		Debug.Log("Tap");
		Ray ray = Camera.main.ScreenPointToRay(mousePosition);
		RaycastHit hit;
		if(Physics.Raycast(ray, out hit)) {
			controller.pivotObject = hit.collider.gameObject;
		}
	}
}