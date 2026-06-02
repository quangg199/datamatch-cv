import React, { useState } from 'react';
import apiClient from '../api/client';
import { UploadCloud, CheckCircle, Loader2 } from 'lucide-react';

export default function CvUploader({ onCvExtracted }) {
  const [file, setFile] = useState(null);
  const [resumeId, setResumeId] = useState(null);
  const [status, setStatus] = useState('IDLE'); // IDLE, UPLOADING, UPLOADED, EXTRACTING, EXTRACTED, ERROR
  const [skills, setSkills] = useState([]);
  const [errorMsg, setErrorMsg] = useState(null);

  const handleFileChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0]);
      setStatus('IDLE');
      setResumeId(null);
      setSkills([]);
      setErrorMsg(null);
    }
  };

  const handleUpload = async () => {
    if (!file) return;
    setStatus('UPLOADING');
    setErrorMsg(null);

    const formData = new FormData();
    formData.append('file', file);

    try {
      const res = await apiClient.post('/cvs/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      setResumeId(res.data.id);
      setStatus('UPLOADED');
    } catch (err) {
      console.error(err);
      setStatus('ERROR');
      setErrorMsg('Failed to upload CV. Please ensure it is a valid PDF.');
    }
  };

  const handleExtract = async () => {
    if (!resumeId) return;
    setStatus('EXTRACTING');
    setErrorMsg(null);

    try {
      const res = await apiClient.post(`/cvs/${resumeId}/extract-skills`);
      setSkills(res.data.skills || []);
      setStatus('EXTRACTED');
      onCvExtracted(resumeId, res.data.skills || []);
    } catch (err) {
      console.error(err);
      setStatus('ERROR');
      setErrorMsg('Failed to extract skills. The AI service might be busy.');
    }
  };

  return (
    <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 h-full flex flex-col">
      <h2 className="text-xl font-bold text-slate-800 mb-6">1. Candidate Profile</h2>
      
      {errorMsg && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg">
          {errorMsg}
        </div>
      )}

      <div className="mb-6">
        <label className="block text-sm font-medium text-slate-700 mb-2">Upload CV (PDF format)</label>
        <div className="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-slate-300 border-dashed rounded-xl hover:border-blue-400 transition-colors bg-slate-50">
          <div className="space-y-1 text-center">
            <UploadCloud className="mx-auto h-12 w-12 text-slate-400" />
            <div className="flex text-sm text-slate-600 justify-center">
              <label htmlFor="cv-upload" className="relative cursor-pointer bg-white rounded-md font-medium text-blue-600 hover:text-blue-500 focus-within:outline-none focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-blue-500 px-1">
                <span>Upload a file</span>
                <input id="cv-upload" name="cv-upload" type="file" accept=".pdf" className="sr-only" onChange={handleFileChange} />
              </label>
              <p className="pl-1">or drag and drop</p>
            </div>
            <p className="text-xs text-slate-500">PDF up to 10MB</p>
          </div>
        </div>
        {file && (
          <div className="mt-3 flex items-center justify-between p-3 bg-slate-50 rounded-lg border border-slate-200">
            <span className="text-sm text-slate-700 truncate mr-4">{file.name}</span>
            {status === 'IDLE' && (
              <button onClick={handleUpload} className="px-3 py-1 bg-slate-800 text-white text-xs font-medium rounded-md hover:bg-slate-700">
                Upload
              </button>
            )}
            {status === 'UPLOADING' && <Loader2 className="w-4 h-4 text-blue-600 animate-spin" />}
            {['UPLOADED', 'EXTRACTING', 'EXTRACTED'].includes(status) && <CheckCircle className="w-5 h-5 text-green-500" />}
          </div>
        )}
      </div>

      <div className="mt-auto pt-4 border-t border-slate-100">
        <button
          onClick={handleExtract}
          disabled={status !== 'UPLOADED'}
          className={`w-full flex justify-center items-center py-3 px-4 border border-transparent rounded-xl shadow-sm text-sm font-medium text-white ${
            status === 'UPLOADED' ? 'bg-blue-600 hover:bg-blue-700' : 'bg-slate-300 cursor-not-allowed'
          } transition-colors`}
        >
          {status === 'EXTRACTING' ? (
            <><Loader2 className="w-4 h-4 mr-2 animate-spin" /> Extracting AI Skills...</>
          ) : (
            'Extract Skills'
          )}
        </button>
      </div>

      {status === 'EXTRACTED' && (
        <div className="mt-6 flex-1 overflow-y-auto min-h-0">
          <h3 className="text-sm font-semibold text-slate-800 mb-3 uppercase tracking-wider">Extracted Skills</h3>
          <div className="flex flex-wrap gap-2">
            {skills.map((skill, idx) => (
              <span key={idx} className="px-3 py-1 bg-blue-50 text-blue-700 text-xs font-medium rounded-full border border-blue-100">
                {skill}
              </span>
            ))}
            {skills.length === 0 && <span className="text-sm text-slate-500 italic">No skills found.</span>}
          </div>
        </div>
      )}
      {/* Popup thông báo AI đang chạy */}
      {status === 'EXTRACTING' && (
        <div className="fixed bottom-6 right-6 bg-white border-l-4 border-blue-500 shadow-2xl rounded-xl p-4 flex items-center space-x-4 z-50">
          <Loader2 className="w-6 h-6 text-blue-500 animate-spin" />
          <div>
            <p className="text-sm font-bold text-slate-800">Đang trích xuất AI...</p>
            <p className="text-xs text-slate-600">AI đang trích xuất dữ liệu các kĩ năng của bạn, vui lòng chờ...</p>
          </div>
        </div>
      )}
    </div>
  );
}
