using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI; 
using System;
using UnityEngine.EventSystems;


public class SelectElectronics : MonoBehaviour, IPointerClickHandler
{

    public List<GameObject> Prefabs;
    public int currentSelectedPrefab;
    public GameObject selectedPrefab;
    private GameObject attachedObject;
    public float yOffset = 0f;

    public void OnPointerClick(PointerEventData eventData)
    {


        switch (gameObject.name)
        {
            case "LedIm":
                currentSelectedPrefab = 0;
                ChangeSelectedPrefab();

                break;
            case "ResistorIm":
                currentSelectedPrefab = 1;
                ChangeSelectedPrefab();
                break;
            case "BoardIm":
                currentSelectedPrefab = 2;
                ChangeSelectedPrefab();

                break;
            case "ArduinoIm":
                currentSelectedPrefab = 3;
                ChangeSelectedPrefab();
                break;
            case "BatteryIm":
                currentSelectedPrefab = 4;
                ChangeSelectedPrefab();
                break;
            case "ransistorIm":
                currentSelectedPrefab = 5;
                ChangeSelectedPrefab();
                break;
            case "ButtonIm":
                currentSelectedPrefab = 6;
                ChangeSelectedPrefab();
                break;
            case "Battery1.5Im":
                currentSelectedPrefab = 7;
                ChangeSelectedPrefab();
                break;
            case "DiodIm":
                currentSelectedPrefab = 8;
                ChangeSelectedPrefab();
                break;
            case "DisplayIm":
                currentSelectedPrefab = 9;
                ChangeSelectedPrefab();
                break;
            case "GasIm":
                currentSelectedPrefab = 10;
                ChangeSelectedPrefab();
                break;
            case "InfraredIm":
                currentSelectedPrefab = 11;
                ChangeSelectedPrefab();
                break;
            case "JoystickIm":
                currentSelectedPrefab = 12;
                ChangeSelectedPrefab();
                break;
            case "LampIm":
                currentSelectedPrefab = 13;
                ChangeSelectedPrefab();
                break;
            case "Motor1Im":
                currentSelectedPrefab = 14;
                ChangeSelectedPrefab();
                break;
            case "Motor2Im":
                currentSelectedPrefab = 15;
                ChangeSelectedPrefab();
                break;
            case "PezoIm":
                currentSelectedPrefab = 16;
                ChangeSelectedPrefab();
                break;
            case "PhotoIm":
                currentSelectedPrefab = 17;
                ChangeSelectedPrefab();
                break;
            case "PirIm":
                currentSelectedPrefab = 18;
                ChangeSelectedPrefab();
                break;
            case "PotentIm":
                currentSelectedPrefab = 19;
                ChangeSelectedPrefab();
                break;
            case "RelayIm":
                currentSelectedPrefab = 20;
                ChangeSelectedPrefab();
                break;
            case "RfidIm":
                currentSelectedPrefab = 21;
                ChangeSelectedPrefab();
                break;
            case "ServoIm":
                currentSelectedPrefab = 22;
                ChangeSelectedPrefab();
                break;
            case "SolarIm":
                currentSelectedPrefab = 23 ;
                ChangeSelectedPrefab();
                break;
            case "SonarIm":
                currentSelectedPrefab = 24;
                ChangeSelectedPrefab();
                break;
            case "SwitchIm":
                currentSelectedPrefab = 25 ;
                ChangeSelectedPrefab();
                break;
            case "TempIm":
                currentSelectedPrefab = 26;
                ChangeSelectedPrefab();
                break;
            case "TimeIm":
                currentSelectedPrefab = 27;
                ChangeSelectedPrefab();
                break;
            case "VibroIm":
                currentSelectedPrefab = 28;
                ChangeSelectedPrefab();
                break;
            case "VoltIm":
                currentSelectedPrefab = 30;
                ChangeSelectedPrefab();
                break;
            case "TransNMOSTIm":
                currentSelectedPrefab = 31;
                ChangeSelectedPrefab();
                break;
            case "TransPIm":
                currentSelectedPrefab = 32;
                ChangeSelectedPrefab();
                break;
            case "TransPMOSIm":
                currentSelectedPrefab = 33;
                ChangeSelectedPrefab();
                break;
        }

    }


    // Use this for initialization
    void Start()
    {
        Destroy(selectedPrefab);
        
        ChangeSelectedPrefab();

    }

    // Update is called once per frame
    void Update()
    {
        /*
        if (Physics.Raycast(Camera.main.ScreenPointToRay(Input.mousePosition), out hit))
        {
            Vector3 rawPosition = hit.point;
            rawPosition.x = (float)Math.Round(rawPosition.x, 1);
            rawPosition.y = (float)Math.Round(rawPosition.y, 1);
            rawPosition.z = (float)Math.Round(rawPosition.z, 1);

            rawPosition.y += ((selectedPrefab.GetComponent<Collider>().bounds.size.y / 2f) + yOffset);

            attachedObject = hit.collider.gameObject;
            selectedPrefab.transform.position = rawPosition;
        }

        if (Input.GetMouseButtonDown(0))
        {
            selectedPrefab.GetComponent<Rigidbody>().detectCollisions = true;
            selectedPrefab.GetComponent<Rigidbody>().isKinematic = false;
            Debug.Log(selectedPrefab.transform.position);

            attachedObject.AddComponent<FixedJoint>().connectedBody = selectedPrefab.GetComponent<Rigidbody>();
            ChangeSelectedPrefab();
            Destroy(selectedPrefab);
        }

        if (Input.GetKeyDown(KeyCode.UpArrow))
        {
            yOffset += 0.1f;
        }
        else if (Input.GetKeyDown(KeyCode.DownArrow))
        {
            yOffset -= 0.1f;
        }*/
    }

    void ChangeSelectedPrefab()
    {
        selectedPrefab = null;
        selectedPrefab = Instantiate(Prefabs[currentSelectedPrefab]);
    }
}

