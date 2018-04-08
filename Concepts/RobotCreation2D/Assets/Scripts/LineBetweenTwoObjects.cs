using UnityEngine;

public class LineBetweenTwoObjects : MonoBehaviour {
	LineRenderer attachedRenderer;
	public Material materialToSet;

	public GameObject object1, object2;
	void Start() {
		attachedRenderer = gameObject.AddComponent<LineRenderer>();
		attachedRenderer.startWidth = 0.03f;
		attachedRenderer.endWidth = 0.03f;
		attachedRenderer.material = materialToSet;
		attachedRenderer.sortingOrder = -1;
		attachedRenderer.SetPositions(new Vector3[] { object1.transform.position, object2.transform.position });
	}

	void Update() {
		attachedRenderer.SetPosition(0, object1.transform.position);
		attachedRenderer.SetPosition(1, object2.transform.position);
	}
}