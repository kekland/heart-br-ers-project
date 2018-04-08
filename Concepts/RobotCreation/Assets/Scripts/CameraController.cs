using UnityEngine;

public class CameraController : MonoBehaviour {
	public Camera camera;
	public GameObject pivotObject;
	public float distanceFromObject = 10f;
	public float multiplier = 0.01f;
	public float angleX;
	public float angleY;

	Vector3 desiredPosition;

	void Start() {
		clickDelta = Mathf.Clamp(Screen.height * 0.01f, 1f, 7f);
	}
	public float clickDelta = 1f;
	public float clickTimeDelta = 0.1f;
	Vector2 clickStart;
	float clickStartTime;
	bool isValidClick = false;
	public float cameraLerpSpeed = 10f;

	public delegate void OnTappedDelegate(Vector2 position);

	public event OnTappedDelegate OnTapped;
	Vector2? prevTouchPosition = null;

	Vector2?[] oldTouchPositions = {
		null,
		null
	};
	Vector2 oldTouchVector;
	float oldTouchDistance;

	float fixAngle(float a) {
		if(a >= 0f && a < 360f) {
			return a;
		}
		if(a < 0f) {
			a += 360f;
		}
		a %= 360f;
		return fixAngle(a);
	}
	Vector3 velocity;
	public float smoothTime;
	public void UpdateCamera() {
		Debug.Log(Input.touchCount);
		HandleClicks();
		HandleMovements();
		HandleZoom();

		//angleX = fixAngle(angleX);
		//angleY = fixAngle(angleY);

		desiredPosition = Quaternion.AngleAxis(angleX, -Vector3.up) * Quaternion.AngleAxis(angleY, -Vector3.right) * new Vector3(0, 0, distanceFromObject) + pivotObject.transform.position;

		camera.transform.position = Vector3.Lerp(camera.transform.position, desiredPosition, smoothTime * Time.deltaTime);

		//camera.transform.LookAt(pivotObject.transform);
		var targetRotation = Quaternion.LookRotation(pivotObject.transform.position - camera.transform.position);

		// Smoothly rotate towards the target point.
		camera.transform.rotation = Quaternion.Slerp(camera.transform.rotation, targetRotation, cameraLerpSpeed * Time.deltaTime);
	}


	void HandleZoom() {
		if (Input.touchCount == 0)
		{
			oldTouchPositions[0] = null;
			oldTouchPositions[1] = null;
		}
		else if (Input.touchCount == 1)
		{
			if (oldTouchPositions[0] == null || oldTouchPositions[1] != null)
			{
				oldTouchPositions[0] = Input.GetTouch(0).position;
				oldTouchPositions[1] = null;
			}
			else
			{
				Vector2 newTouchPosition = Input.GetTouch(0).position;

				//transform.position += transform.TransformDirection((Vector3)((oldTouchPositions[0] - newTouchPosition) * camera.orthographicSize / camera.pixelHeight * 2f));

				oldTouchPositions[0] = newTouchPosition;
			}
		}
		else
		{
			if (oldTouchPositions[1] == null)
			{
				oldTouchPositions[0] = Input.GetTouch(0).position;
				oldTouchPositions[1] = Input.GetTouch(1).position;
				oldTouchVector = (Vector2)(oldTouchPositions[0] - oldTouchPositions[1]);
				oldTouchDistance = oldTouchVector.magnitude;
			}
			else
			{
				Vector2 screen = new Vector2(camera.pixelWidth, camera.pixelHeight);

				Vector2[] newTouchPositions = {
					Input.GetTouch(0).position,
					Input.GetTouch(1).position
				};
				Vector2 newTouchVector = newTouchPositions[0] - newTouchPositions[1];
				float newTouchDistance = newTouchVector.magnitude;

				distanceFromObject *= oldTouchDistance / newTouchDistance;

				oldTouchPositions[0] = newTouchPositions[0];
				oldTouchPositions[1] = newTouchPositions[1];
				oldTouchVector = newTouchVector;
				oldTouchDistance = newTouchDistance;
			}
		}
	}

	void HandleMovements() {
		if (Input.touchCount == 1)
		{
			Vector2 currentTouch = Input.GetTouch(0).position;
			if (prevTouchPosition == null)
			{
				prevTouchPosition = currentTouch;
			}
			else
			{
				Vector2 deltaMovement = (Vector2)prevTouchPosition - currentTouch;
				angleX += deltaMovement.x * multiplier;
				angleY += deltaMovement.y * multiplier;

				angleY = Mathf.Clamp(angleY, -89f, 89f);
				prevTouchPosition = currentTouch;
			}
		}
		else
		{
			prevTouchPosition = null;
		}
	}

	void HandleClicks() {
		if (Input.touchCount == 1 && Input.GetTouch(0).phase == TouchPhase.Began && !isValidClick)
		{
			isValidClick = true;
			clickStartTime = Time.time;
		}
		if (isValidClick)
		{
			if (Input.touchCount > 1)
			{
				isValidClick = false;
			}
			if (Input.touchCount == 1 && (Input.GetTouch(0).deltaPosition.magnitude > clickDelta ||
				 Time.time - clickStartTime > clickTimeDelta))
			{
				isValidClick = false;
			}
		}
		if (Input.touchCount == 0)
		{
			if (isValidClick)
			{
				if (OnTapped != null)
				{
					OnTapped(Input.mousePosition);
				}
			}
			isValidClick = false;
			clickStart = Vector2.zero;
			clickStartTime = 0f;
		}
	}
}