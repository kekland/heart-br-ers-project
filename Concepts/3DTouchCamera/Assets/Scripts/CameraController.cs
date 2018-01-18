using UnityEngine;

public class CameraController : MonoBehaviour {
	public Camera camera;
	public GameObject pivotObject;
	public float distanceFromObject = 10f;
	public float multiplier = 0.01f;
	public float angleX;
	public float angleY;
	Vector2? prevTouchPosition = null;
	void Update() {
		if(Input.touchCount == 1) {
			Vector2 currentTouch = Input.GetTouch(0).position;
			if(prevTouchPosition == null) {
				prevTouchPosition = currentTouch;
			}
			else {
				Vector2 deltaMovement = (Vector2)prevTouchPosition - currentTouch;
				angleX += deltaMovement.x * multiplier;
				angleY += deltaMovement.y * multiplier;

				angleY = Mathf.Clamp(angleY, -90f, 90f);
				prevTouchPosition = currentTouch;
			}
		}
		else {
			prevTouchPosition = null;
		}

		Vector3 posBefore = camera.transform.position;
		camera.transform.position = Quaternion.AngleAxis(angleX, -Vector3.up) * Quaternion.AngleAxis(angleY, -Vector3.right) * new Vector3(0, 0, distanceFromObject);

		camera.transform.LookAt(pivotObject.transform);

		Debug.DrawLine(camera.transform.position, posBefore, Color.blue, 100000f, false);
	}
}