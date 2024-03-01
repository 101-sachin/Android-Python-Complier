
import flask
import os
import subprocess
from flask_cors import CORS
import platform

def whatsplatform(): #--> str
    return "windows" if platform.system() == "Windows" else "linux"
    


app = flask.Flask(__name__)
CORS(app)  # Initialize CORS extension



codetypes = {
    "python": "py",
    "javascript": "js",
    # Add other supported languages here
}

@app.route('/compiler', methods=['POST'])
def receive_data():
    data = flask.request.json
    lang = "python3" if whatsplatform() == "linux" else "python"
    

    if data['language'] not in codetypes:
        return "error", 400

    try:
        # Write the code to a file
        file_extension = codetypes[data['language']]
        with open(f"test.{file_extension}", "w") as file:
            file.write(data['code'])

        if file_extension == "py":
            result = subprocess.run([lang, 'test.py'], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
            output = result.stdout + result.stderr
        else:
            output = "Language not supported yet"

        response_data = {"output": output}
        return flask.jsonify(response_data), 200, {
            'Access-Control-Allow-Origin': '*'  # Add the CORS header
        }
    except Exception as e:
        return flask.jsonify({"error": str(e)}), 500, {
            'Access-Control-Allow-Origin': '*'  # Add the CORS header
        }

if __name__ == "__main__":
<<<<<<< HEAD
    app.run(host='192.168.43.46', port=3000, debug=True)
=======
    app.run(host="192.168.2.150", port=8080, debug=True)
>>>>>>> 03d33f7d334d77e59f95de47178cea562c453aea
